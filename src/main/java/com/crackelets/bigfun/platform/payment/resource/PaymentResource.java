package com.crackelets.bigfun.platform.payment.resource;

import com.crackelets.bigfun.platform.booking.resource.EventAttendeeResource;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResource {
    private Long id;
    private Date date;
    private String qrImg;
    private EventAttendeeResource eventAttendee;
}
