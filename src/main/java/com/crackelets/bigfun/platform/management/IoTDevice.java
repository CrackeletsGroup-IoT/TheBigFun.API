package com.crackelets.bigfun.platform.management;

import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import jdk.javadoc.doclet.Taglet;
import lombok.*;
import lombok.extern.java.Log;

import javax.persistence.*;
import java.math.BigDecimal;

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

    private BigDecimal latitude;
    private BigDecimal longitude;

    private Integer pulse;

    @OneToOne
    @JoinColumn(name = "event_attendee_id")
    private EventAttendee eventAttendee;


}
