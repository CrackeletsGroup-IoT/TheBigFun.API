package com.crackelets.bigfun.platform.management.resource;

import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class IoTDeviceResource {
    private Long id;
    private double distanceP1;
    private double distanceP2;
    private double distanceBetween;
    private double pulse;
    private boolean safe;
    private boolean danger;
    private String time;
    private Long eventAttendeeId;
}
