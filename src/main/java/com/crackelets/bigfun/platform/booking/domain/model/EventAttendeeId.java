package com.crackelets.bigfun.platform.booking.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class EventAttendeeId implements Serializable {

    private Long eventId;
    private Long attendeeId;


    public EventAttendeeId(Long eventId, Long attendeeId) {
        this.eventId = eventId;
        this.attendeeId = attendeeId;
    }

    public EventAttendeeId() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventAttendeeId that = (EventAttendeeId) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(attendeeId, that.attendeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, attendeeId);
    }
}
