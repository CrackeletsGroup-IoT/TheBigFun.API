package com.crackelets.bigfun.platform.booking.api.rest;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventPaymentRepository;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventRepository;
import com.crackelets.bigfun.platform.booking.domain.service.EventPaymentService;
import com.crackelets.bigfun.platform.booking.mapping.EventPaymentMapper;
import com.crackelets.bigfun.platform.booking.resource.EventPaymentResource;
import com.crackelets.bigfun.platform.payment.resource.PaymentResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value= "/api/v1/eventsp")
public class EventPaymentController {

    private final EventPaymentService eventPaymentService;

    private final EventRepository eventRepository;

    private final EventPaymentRepository eventPaymentRepository;

    private final EventPaymentMapper mapper;

    public EventPaymentController(EventPaymentService eventPaymentService, EventRepository eventRepository, EventPaymentRepository eventPaymentRepository, EventPaymentMapper mapper) {
        this.eventPaymentService = eventPaymentService;
        this.eventRepository = eventRepository;
        this.eventPaymentRepository = eventPaymentRepository;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<EventPaymentResource> getAllEventsPayment(Pageable pageable){
        return mapper.modelListPage(eventPaymentService.getAll(),pageable);
    }

    @GetMapping("{eventId}/payments")
    public Page<EventPaymentResource> getAllPaymentsByEventId(@PathVariable Long eventId,Pageable pageable){
        return mapper.modelListPage(eventPaymentService.getAllPaymentByEventId(eventId),pageable);
    }

    @PostMapping("{eventId}/payments/{paymentId}")
    public ResponseEntity<?>addPaymentToEvent(@PathVariable Long eventId,@PathVariable Long paymentId){
        Event event=eventRepository.findById(eventId).orElseThrow(()->new RuntimeException("The event doesn't exist"));

        //event.addPayment(event,paymentId);
        eventRepository.save(event);

        return ResponseEntity.ok("Payment was added correctly");
    }

}
