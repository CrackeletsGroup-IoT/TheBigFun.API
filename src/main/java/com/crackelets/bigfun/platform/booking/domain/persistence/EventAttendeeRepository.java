package com.crackelets.bigfun.platform.booking.domain.persistence;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {

    Optional<EventAttendee> findEventAttendeeById(Long id);

    List<EventAttendee> findAllByEvent_Id(Long eventId);

    List<EventAttendee> findAllByAttendee_Id(Long attendeeId);
}
