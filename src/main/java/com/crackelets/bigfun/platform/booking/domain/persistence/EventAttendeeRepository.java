package com.crackelets.bigfun.platform.booking.domain.persistence;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {

    Optional<EventAttendee> findEventAttendeeById(Long id);

    List<EventAttendee> findAllByEvent_Id(Long eventId);

    List<EventAttendee> findAllByAttendee_Id(Long attendeeId);

    Optional<EventAttendee> findEventAttendeeByPayment_Id(Long paymentId);

    Optional<EventAttendee> findEventAttendeeByIoTDevice_Id(Long ioTDeviceId);



}
