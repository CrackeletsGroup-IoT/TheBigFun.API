package com.crackelets.bigfun.platform.payment.api.rest;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.payment.domain.model.Payment;
import com.crackelets.bigfun.platform.payment.domain.service.PaymentService;
import com.crackelets.bigfun.platform.payment.mapping.PaymentMapper;
import com.crackelets.bigfun.platform.payment.resource.CreatePaymentResource;
import com.crackelets.bigfun.platform.payment.resource.PaymentResource;
import com.crackelets.bigfun.platform.shared.services.media.StorageService;
import com.crackelets.bigfun.platform.storage.domain.MyFile;
import com.crackelets.bigfun.platform.storage.service.MyFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
private final PaymentService paymentService;
private final PaymentMapper mapper;

    @Autowired
    private MyFileService fileService;


    public PaymentController(PaymentService paymentService, PaymentMapper mapper, StorageService storageService) {
        this.paymentService = paymentService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<PaymentResource> getAllPayments(Pageable pageable){
        return mapper.modelListPage(paymentService.getAll(), pageable);
    }
    @GetMapping("{paymentId}")
    public PaymentResource getPaymentById(@PathVariable Long paymentId){
        return mapper.toResource(paymentService.getById(paymentId));
    }
    @PostMapping
    public ResponseEntity<PaymentResource> createPayment(@RequestBody CreatePaymentResource resource){
        return  new ResponseEntity<>(mapper.toResource(paymentService.create(mapper.toModel(resource))), HttpStatus.CREATED);
    }
    @DeleteMapping("{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId){
        return paymentService.delete(paymentId);
    }

    @PostMapping("{paymentId}/qr")
    public ResponseEntity<Payment> uploadFiles(@PathVariable Long paymentId, @RequestParam("file") MultipartFile file) throws IOException {

        Payment payment = paymentService.getById(paymentId);

        if (payment == null) return ResponseEntity.notFound().build();

        String uploadedFile = this.fileService.uploadFile(file, paymentId + "-qr");

        payment.setQrImg(uploadedFile);
        Payment postWithImages= paymentService.update(paymentId, payment);

        return ResponseEntity.ok(postWithImages);
    }
}
