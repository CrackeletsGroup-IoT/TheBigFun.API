package com.crackelets.bigfun.platform.booking.api.rest;

import com.crackelets.bigfun.platform.booking.domain.persistence.EventAttendeeRepository;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventRepository;
import com.crackelets.bigfun.platform.booking.domain.service.EventAttendeeService;
import com.crackelets.bigfun.platform.booking.mapping.EventAttendeeMapper;
import com.crackelets.bigfun.platform.booking.mapping.EventMapper;
import com.crackelets.bigfun.platform.booking.resource.AttendeeByEventResource;
import com.crackelets.bigfun.platform.booking.resource.EventAttendeeResource;
import com.crackelets.bigfun.platform.booking.resource.EventResource;
import com.crackelets.bigfun.platform.profile.mapping.AttendeeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value= "/api/v1/event-attendees")
public class EventAttendeeController {

    private final EventAttendeeService eventAttendeeService;

    private final EventRepository eventRepository;

    private final EventAttendeeRepository eventAttendeeRepository;

    private final EventAttendeeMapper mapper;

    private final AttendeeMapper attendeeMapper;

    private final EventMapper eventMapper;

    private final EventAttendeeMapper eventAttendeeMapper;

    public EventAttendeeController(EventAttendeeService eventAttendeeService, EventRepository eventRepository, EventAttendeeRepository eventAttendeeRepository, EventAttendeeMapper mapper, AttendeeMapper attendeeMapper, EventMapper eventMapper, EventAttendeeMapper eventAttendeeMapper) {
        this.eventAttendeeService = eventAttendeeService;
        this.eventRepository = eventRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.mapper = mapper;
        this.attendeeMapper = attendeeMapper;
        this.eventMapper = eventMapper;
        this.eventAttendeeMapper = eventAttendeeMapper;
    }


    @GetMapping
    public Page<EventAttendeeResource> getAllEventsAttendee(Pageable pageable){
        return mapper.modelListPage(eventAttendeeService.getAll(),pageable);
    }

    @GetMapping("attendees/{attendeeId}")
    public Page<EventResource> getAllEventsByAttendeeId(@PathVariable Long attendeeId, Pageable pageable){
        return eventMapper.modelListPage(eventAttendeeService.getAllByAttendeeId(attendeeId),pageable);
    }

    @GetMapping("events/{eventId}")
    public Page<AttendeeByEventResource> getAllAttendeesByEvent(@PathVariable Long eventId, Pageable pageable){
        return eventAttendeeMapper.toAttendeeByEventResource(eventAttendeeService.getAllAttendeesByEventId(eventId),pageable);
    }

    @PostMapping("event-{eventId}/attendee-{attendeeId}")
    public ResponseEntity<EventAttendeeResource> createEvent(@PathVariable Long eventId, @PathVariable Long attendeeId){
        return new ResponseEntity<>(mapper.toResource(eventAttendeeService.create(eventId, attendeeId)), HttpStatus.CREATED);
    }

    //GetByEventAttendeId
    @GetMapping("/{id}")
    public ResponseEntity<EventAttendeeResource> getEventAttendeeById(@PathVariable Long id){
        return new ResponseEntity<>(mapper.toResource(eventAttendeeService.getEventAttendeeById(id)), HttpStatus.OK);
    }


}
