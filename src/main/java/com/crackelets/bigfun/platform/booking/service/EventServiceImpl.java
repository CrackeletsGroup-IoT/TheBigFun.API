package com.crackelets.bigfun.platform.booking.service;
import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventRepository;
import com.crackelets.bigfun.platform.booking.domain.service.EventService;
import com.crackelets.bigfun.platform.profile.domain.model.Organizer;
import com.crackelets.bigfun.platform.profile.domain.persistence.OrganizerRepository;
import com.crackelets.bigfun.platform.shared.exception.ResourceNotFoundException;
import com.crackelets.bigfun.platform.shared.exception.ResourceValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {


    private static final String ENTITY = "Event";

    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;

    private final Validator validator;

    public EventServiceImpl(EventRepository eventRepository, OrganizerRepository organizerRepository, Validator validator) {
        this.eventRepository = eventRepository;
        this.organizerRepository = organizerRepository;
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
    public Event create(Event event, Long organizerId) {
        Set<ConstraintViolation<Event>> violations= validator.validate(event);

        if (!violations.isEmpty())
            throw  new ResourceValidationException(ENTITY, violations);

        Event eventWithName = eventRepository.findByName(event.getName());

        if(eventWithName != null)
            throw new ResourceValidationException(ENTITY, "An event with the same name already exists. ");

        Optional<Organizer> organizerOptional = organizerRepository.findOrganizerById(organizerId);

        if (organizerOptional.isPresent()){
            Organizer organizer = organizerOptional.get();
            event.setOrganizer(organizer);
            return eventRepository.save(event);
        }

       throw new RuntimeException("Organizer not found with id " + organizerId);

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
                        .withDescription(event.getDescription())
                        .withHour(event.getHour())
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
   @Transactional
    public List<Event> getAllByOrganizerId(Long id) {
        return eventRepository.findAllByOrganizerId(id);
    }

}
