package com.crackelets.bigfun.platform.payment.domain.service;

import com.crackelets.bigfun.platform.payment.domain.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {
    List<Payment> getAll();
    Page<Payment> getAll(Pageable pageable);
    Payment getById(Long paymentId);
    Payment create(Payment payment);
    Payment update(Long id, Payment payment);
    ResponseEntity<?> delete(Long paymentId);
    Payment getByUuid(String uuid);

}
