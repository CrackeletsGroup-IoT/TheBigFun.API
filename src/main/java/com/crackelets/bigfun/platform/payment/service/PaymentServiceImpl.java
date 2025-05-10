package com.crackelets.bigfun.platform.payment.service;

import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventAttendeeRepository;
import com.crackelets.bigfun.platform.payment.domain.model.Payment;
import com.crackelets.bigfun.platform.payment.domain.persistence.PaymentRepository;
import com.crackelets.bigfun.platform.payment.domain.service.PaymentService;
import com.crackelets.bigfun.platform.shared.exception.ResourceNotFoundException;
import com.crackelets.bigfun.platform.shared.exception.ResourceValidationException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String ENTITY = "Payment";
    private final PaymentRepository paymentRepository;
    private final EventAttendeeRepository eventAttendeeRepository;
    private final Validator validator;

    public PaymentServiceImpl(PaymentRepository paymentRepository, EventAttendeeRepository eventAttendeeRepository, Validator validator) {
        this.paymentRepository = paymentRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.validator = validator;
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Page<Payment> getAll(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    @Override
    public Payment getById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, paymentId));
    }


    @Override
    public Payment create(Payment payment, Long eventAttendeeId) {
        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);

        if(!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        Optional<EventAttendee> eventAttendeeOptional = eventAttendeeRepository.findEventAttendeeById(eventAttendeeId);

        if (eventAttendeeOptional.isPresent()){
            EventAttendee eventAttendee = eventAttendeeOptional.get();

            // Guardar Payment primero
            Payment savedPayment = paymentRepository.save(payment);
            eventAttendee.setPayment(savedPayment);
            eventAttendeeRepository.save(eventAttendee);
            return savedPayment;

        }

        throw new RuntimeException("EventAttendee with id" + eventAttendeeId + " not found");

    }




    @Override
    public Payment update(Long id, Payment payment) {
        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);

        if (!violations.isEmpty())
            throw  new ResourceValidationException(ENTITY, violations);

        return paymentRepository.findById(id).map(paymentToUpdate -> paymentRepository.save(
            paymentToUpdate.withDate(payment.getDate()).withQrImg(payment.getQrImg())))
          .orElseThrow(() -> new ResourceNotFoundException(ENTITY, id));
    }

    @Override
    public ResponseEntity<?> delete(Long paymentId) {
        return paymentRepository.findById(paymentId).map(payment ->{
            paymentRepository.delete(payment);
            return ResponseEntity.ok().build();})
                .orElseThrow(()->new ResourceNotFoundException(ENTITY,paymentId));
    }

    @Override
    public Payment getByUuid(String uuid) {
        return paymentRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY));
    }

    // notification for my n8n
    public void notifyN8nAfterPayment(Payment payment, EventAttendee eventAttendee) {
        String n8nWebhookUrl = "http://localhost:5678/webhook/payment-confirmation";
        RestTemplate restTemplate = new RestTemplate();



        Map<String, Object> payload = Map.of(
                "paymentId", payment.getId(),
                "uuid", payment.getUuid(),
                "amount", eventAttendee.getEvent().getCost(),
                "buyerEmail", eventAttendee.getAttendee().getEmail(),
                "eventName", eventAttendee.getEvent().getName(),
                "eventDate", eventAttendee.getEvent().getDate(),
                "eventAddress", eventAttendee.getEvent().getAddress(),
                "qrImg", payment.getQrImg()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Auth-Token", "...");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(n8nWebhookUrl, requestEntity, String.class);
            System.out.println("Notificación enviada a n8n. Respuesta: " + response.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error al notificar a n8n: " + e.getMessage());
        }
    }



}
