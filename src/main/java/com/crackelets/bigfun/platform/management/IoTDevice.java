package com.crackelets.bigfun.platform.management;

import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@Entity
@Table(name = "iot_devices")
public class IoTDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distanceP1;

    private double distanceP2;

    private double distanceBetween;

    private double pulse;

    private boolean safe;

    private boolean danger;

    private String time;

    @OneToOne
    @JoinColumn(name = "event_attendee_id")
    private EventAttendee eventAttendee;


}
