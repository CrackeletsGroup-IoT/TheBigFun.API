package com.crackelets.bigfun.platform.booking.domain.service;


import com.crackelets.bigfun.platform.booking.domain.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {

    List <Event> getAll();

    Page<Event> getAll(Pageable pageable);

    Event getById(long eventId);


    Event create(Event event, Long organizerId);
    Event update(Long eventId, Event event);

    ResponseEntity<?> delete(Long eventId);

    List<Event> getAllByOrganizerId(Long id);
    Event addAttendeeToEvent(Long eventId, Long attendeeId);



}
