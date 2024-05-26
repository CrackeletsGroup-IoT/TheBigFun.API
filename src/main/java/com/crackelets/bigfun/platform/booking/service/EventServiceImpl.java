package com.crackelets.bigfun.platform.booking.service;


import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import com.crackelets.bigfun.platform.booking.domain.model.EventAttendeeId;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventAttendeeRepository;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventRepository;
import com.crackelets.bigfun.platform.booking.domain.service.EventService;
import com.crackelets.bigfun.platform.profile.domain.model.Attendee;
import com.crackelets.bigfun.platform.profile.domain.persistence.AttendeeRepository;
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
public class EventServiceImpl implements EventService {


    private static final String ENTITY = "Event";

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;
    private final EventAttendeeRepository eventAttendeeRepository;

    private final Validator validator;

    public EventServiceImpl(EventRepository eventRepository, AttendeeRepository attendeeRepository, EventAttendeeRepository eventAttendeeRepository, Validator validator) {
        this.eventRepository = eventRepository;
        this.attendeeRepository = attendeeRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.validator = validator;
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public Page<Event> getAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Event getById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, eventId));
    }

    @Override
    public Event create(Event event) {
        Set<ConstraintViolation<Event>> violations= validator.validate(event);

        if (!violations.isEmpty())
            throw  new ResourceValidationException(ENTITY, violations);

        Event eventWithName = eventRepository.findByName(event.getName());

        if(eventWithName != null)
            throw new ResourceValidationException(ENTITY, "An event with the same name already exists. ");

        return eventRepository.save(event);

    }

    @Override
    public Event update(Long eventId, Event event) {
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        if (!violations.isEmpty())
            throw  new ResourceValidationException(ENTITY, violations);

        Event eventWithName = eventRepository.findByName(event.getName());

        if(eventWithName != null && !eventWithName.getId().equals(event.getId()))
            throw new ResourceValidationException(ENTITY, "An event with the same name already exists.");

        return eventRepository.findById(eventId).map(eventToUpdate -> eventRepository.save(
                eventToUpdate.withName(event.getName())
                        .withDate(event.getDate())
                        .withAddress(event.getAddress())
                        .withCost(event.getCost())
                        .withImageUrl(event.getImageUrl())
                        .withCapacity(event.getCapacity())))
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, eventId));

    }

    @Override
    public ResponseEntity<?> delete(Long eventId) {
        return eventRepository.findById(eventId).map(event -> {
            eventRepository.delete(event);
            return  ResponseEntity.ok().build();
        })
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, eventId));
    }

    @Override
    public Event addAttendeeToEvent(Long eventId, Long attendeeId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Optional<Attendee> attendeeOptional = attendeeRepository.findById(attendeeId);

        if (eventOptional.isPresent() && attendeeOptional.isPresent()) {
            Event event = eventOptional.get();
            Attendee attendee = attendeeOptional.get();

            EventAttendee eventAttendee = new EventAttendee();
            eventAttendee.setId(new EventAttendeeId(eventId, attendeeId));
            eventAttendee.setEvent(event);
            eventAttendee.setAttendee(attendee);

            event.getEventAttendees().add(eventAttendee);
            attendee.getEventAttendees().add(eventAttendee);

            eventRepository.save(event);
            attendeeRepository.save(attendee);
            eventAttendeeRepository.save(eventAttendee);

            return event;
        } else {
            throw new RuntimeException("Event or Attendee not found");
        }
    }
}
