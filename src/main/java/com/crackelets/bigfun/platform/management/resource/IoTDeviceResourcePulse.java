package com.crackelets.bigfun.platform.management.resource;

import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class IoTDeviceResourcePulse {
    private Long id;
    private double pulse;
    private boolean safe;
    private boolean danger;

}
