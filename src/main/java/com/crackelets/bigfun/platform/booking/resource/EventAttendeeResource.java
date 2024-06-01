package com.crackelets.bigfun.platform.booking.resource;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.payment.resource.PaymentResource;
import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class EventAttendeeResource {
    private Long id;
    private Long attendeeId;
    private EventResource event;
    private IoTDevice ioTDevice;
    private PaymentResource payment;
}
