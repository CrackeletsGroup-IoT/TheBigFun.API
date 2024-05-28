package com.crackelets.bigfun.platform.payment.api.rest;

import com.crackelets.bigfun.platform.payment.domain.service.PaymentService;
import com.crackelets.bigfun.platform.payment.mapping.PaymentMapper;
import com.crackelets.bigfun.platform.payment.resource.CreatePaymentResource;
import com.crackelets.bigfun.platform.payment.resource.PaymentResource;
import com.crackelets.bigfun.platform.payment.resource.QRCodeRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
private final PaymentService paymentService;
private final PaymentMapper mapper;


    public PaymentController(PaymentService paymentService, PaymentMapper mapper) {
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

    @Value("${qr.api.url}")
    private String qrApiUrl;

    //https://api.qr-code-generator.com/v1/create?access-token=your-acces-token-here

    @Value("${qr.api.access.token}")
    private String qrApiAccessToken;

    @GetMapping("/generateQR")
    public ResponseEntity<?> generateQRCode(@RequestParam("uuid") String uuid) {
        RestTemplate restTemplate = new RestTemplate();

        // Construir la URL del servicio de generación de códigos QR
        String url = qrApiUrl + "?access-token=" + qrApiAccessToken;

        QRCodeRequest qrCodeRequest = new QRCodeRequest();
        qrCodeRequest.setQr_code_text(uuid);
        qrCodeRequest.setImage_format("PNG");

        // Configurar los encabezados
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear la entidad de la solicitud
        HttpEntity<QRCodeRequest> requestEntity = new HttpEntity<>(qrCodeRequest, headers);

        // Realizar la solicitud POST y obtener la respuesta como un arreglo de bytes
        ResponseEntity<?> response = restTemplate.postForEntity(url, requestEntity, byte[].class);

        // Configurar los encabezados de la respuesta
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.IMAGE_PNG);

        // Devolver la respuesta recibida del servicio de generación de códigos QR
        return new ResponseEntity<>(response.getBody(), responseHeaders, response.getStatusCode());
    }

    @DeleteMapping("{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId){
        return paymentService.delete(paymentId);
    }
}
