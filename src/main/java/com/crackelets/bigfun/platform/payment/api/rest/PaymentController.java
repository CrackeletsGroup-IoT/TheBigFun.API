package com.crackelets.bigfun.platform.payment.api.rest;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventAttendeeRepository;
import com.crackelets.bigfun.platform.booking.resource.CreateEventResource;
import com.crackelets.bigfun.platform.booking.resource.EventResource;
import com.crackelets.bigfun.platform.payment.domain.model.Payment;
import com.crackelets.bigfun.platform.payment.domain.service.PaymentService;
import com.crackelets.bigfun.platform.payment.mapping.PaymentMapper;
import com.crackelets.bigfun.platform.payment.resource.CreatePaymentResource;
import com.crackelets.bigfun.platform.payment.resource.PaymentResource;
import com.crackelets.bigfun.platform.payment.resource.QRCodeRequest;
import com.crackelets.bigfun.platform.payment.service.PaymentServiceImpl;
import com.crackelets.bigfun.platform.storage.service.MyFileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
private final PaymentService paymentService;
private final PaymentMapper mapper;
private final MyFileService myFileService;
private final EventAttendeeRepository eventAttendeeRepository;

    public PaymentController(PaymentService paymentService, PaymentMapper mapper, MyFileService myFileService, EventAttendeeRepository eventAttendeeRepository) {
        this.paymentService = paymentService;
        this.mapper = mapper;
      this.myFileService = myFileService;
        this.eventAttendeeRepository = eventAttendeeRepository;
    }

    @Operation(summary = "Get all payments")
    @GetMapping
    public Page<PaymentResource> getAllPayments(Pageable pageable){
        return mapper.modelListPage(paymentService.getAll(), pageable);
    }

    @Operation(summary = "Get a payment by id")
    @GetMapping("{paymentId}")
    public PaymentResource getPaymentById(@PathVariable Long paymentId){
        return mapper.toResource(paymentService.getById(paymentId));
    }

    @PostMapping("{eventAttendeeId}")
    public ResponseEntity<PaymentResource> createPayment(@RequestBody CreatePaymentResource resource, @PathVariable Long eventAttendeeId){
        return  new ResponseEntity<>(mapper.toResource(paymentService.create(mapper.toModel(resource), eventAttendeeId)), HttpStatus.CREATED);
    }


    @Value("${qr.api.url}")
    private String qrApiUrl;

    //https://api.qr-code-generator.com/v1/create?access-token=your-acces-token-here

    @Value("${qr.api.access.token}")
    private String qrApiAccessToken;

    @Operation(summary = "Get a qr for a payment")
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

    @Operation(summary = "Delete a payment")
    @DeleteMapping("{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId){
        return paymentService.delete(paymentId);
    }


/*
    @Operation(summary = "Post QR of a payment")
    @PostMapping("{paymentId}/upload")
    public ResponseEntity<PaymentResource> uploadFiles(@PathVariable Long paymentId, @RequestParam("file") MultipartFile file) throws IOException {

        Payment payment = paymentService.getById(paymentId);
        if (payment == null) return ResponseEntity.notFound().build();

        String stringUrl = myFileService.uploadFile(file, "payment"+ paymentId + "qr.png", "the-big-fun-qr");
        payment.setQrImg(stringUrl);


        Payment postWithImages= paymentService.update(paymentId, payment);




        return ResponseEntity.ok(mapper.toResource(postWithImages));
    }
*/

    @Operation(summary = "Post QR of a payment")
    @PostMapping("{paymentId}/upload")
    public ResponseEntity<PaymentResource> uploadFiles(@PathVariable Long paymentId, @RequestParam("file") MultipartFile file) throws IOException {


        Payment payment = paymentService.getById(paymentId);
        if (payment == null) return ResponseEntity.notFound().build();
        String stringUrl = myFileService.uploadFile(file, "payment" + paymentId + "qr.png", "the-big-fun-qr");
        payment.setQrImg(stringUrl);
        Payment updatedPayment = paymentService.update(paymentId, payment);
        eventAttendeeRepository.findEventAttendeeByPayment_Id(paymentId).ifPresent(eventAttendee -> {
            ((PaymentServiceImpl)paymentService).notifyN8nAfterPayment(updatedPayment, eventAttendee);
        });

        return ResponseEntity.ok(mapper.toResource(updatedPayment));
    }


    @Operation(summary = "Get payment by uuid")
    @GetMapping("/uuid/{uuid}")
    public PaymentResource getPaymentByUuid(@PathVariable String uuid){
        return mapper.toResource(paymentService.getByUuid(uuid));
    }
}
