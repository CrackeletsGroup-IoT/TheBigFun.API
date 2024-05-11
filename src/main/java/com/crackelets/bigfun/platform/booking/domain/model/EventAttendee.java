
package com.crackelets.bigfun.platform.booking.domain.model;

import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.shared.domain.model.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
@Entity
@Table(name="event_attendees")
public class EventAttendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "event_id",nullable = false)
    @JsonIgnore
    private Event event;
    private Long attendeeId;

    @OneToOne(mappedBy = "event_attendee", cascade = CascadeType.ALL)
    private IoTDevice ioTDevice =null;

    public EventAttendee(Event event, Long attendeeId) {
        this.event = event;
        this.attendeeId = attendeeId;
    }




}