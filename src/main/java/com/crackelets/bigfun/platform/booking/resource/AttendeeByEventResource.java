package com.crackelets.bigfun.platform.booking.resource;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResource;
import com.crackelets.bigfun.platform.payment.resource.PaymentResource;
import com.crackelets.bigfun.platform.profile.resource.AttendeeResource;
import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeByEventResource {
    private AttendeeResource attendee;
    private IoTDeviceResource ioTDevice;
}