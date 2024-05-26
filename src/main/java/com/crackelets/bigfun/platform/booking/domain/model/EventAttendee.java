
package com.crackelets.bigfun.platform.booking.domain.model;

import com.crackelets.bigfun.platform.payment.domain.model.Payment;
import com.crackelets.bigfun.platform.profile.domain.model.Attendee;
import com.crackelets.bigfun.platform.shared.domain.model.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
@Entity
@Table(name="event_attendees")
public class EventAttendee implements Serializable {

    @EmbeddedId
    private EventAttendeeId id;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @MapsId("eventId")
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @MapsId("attendeeId")
    private Attendee attendee;

    @OneToOne(mappedBy = "eventAttendee", optional = false, cascade = CascadeType.ALL)
    private Payment payment;

}