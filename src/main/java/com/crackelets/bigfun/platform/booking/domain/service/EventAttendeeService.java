package com.crackelets.bigfun.platform.booking.domain.service;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import com.crackelets.bigfun.platform.profile.domain.model.Attendee;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventAttendeeService {

    List<EventAttendee> getAll();

    EventAttendee getEventAttendeeById(Long id);

    EventAttendee create(Long eventId, Long attendeeId);

    ResponseEntity<?> delete(Long eventAttendeeId);

    List<Event> getAllByAttendeeId(Long attendeeId);

    List<Attendee> getAllAttendeesByEventId(Long eventId);

    EventAttendee addIoTDevice(Long eventAttendeeId, Long ioTDeviceId);
}
