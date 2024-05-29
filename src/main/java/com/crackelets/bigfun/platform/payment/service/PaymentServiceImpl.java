package com.crackelets.bigfun.platform.payment.service;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
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
import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String ENTITY = "Payment";
    private final PaymentRepository paymentRepository;
    private final Validator validator;

    public PaymentServiceImpl(PaymentRepository paymentRepository, Validator validator) {
        this.paymentRepository = paymentRepository;
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
    public Payment create(Payment payment) {
        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);

        if(!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);
        Payment paymentWithDate = paymentRepository.findByDate(payment.getDate());


        return paymentRepository.save(payment);
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
}
