package com.crackelets.bigfun.platform.management.resource;

import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class IoTDeviceResourceDistance {
    private Long id;
    private double distanceP1;
    private double distanceP2;
    private double distanceBetween;

}
