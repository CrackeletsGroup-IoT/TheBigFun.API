package com.crackelets.bigfun.platform.booking.resource;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class EventResource {

    private Long id;
    private String name;
    private String address;
    private int capacity;
    private String date;
    private String imageUrl;
    private int cost;
    private String district;

}
