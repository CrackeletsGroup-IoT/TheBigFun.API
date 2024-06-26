package com.crackelets.bigfun.platform.payment.service;

import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventAttendeeRepository;
import com.crackelets.bigfun.platform.payment.domain.model.Payment;
import com.crackelets.bigfun.platform.payment.domain.persistence.PaymentRepository;
import com.crackelets.bigfun.platform.payment.domain.service.PaymentService;
import com.crackelets.bigfun.platform.shared.exception.ResourceNotFoundException;
import com.crackelets.bigfun.platform.shared.exception.ResourceValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
}
