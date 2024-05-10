package com.crackelets.bigfun.platform.booking.resource;

import lombok.*;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor

public class UpdateEventResource {

    private Long id;
    private String name;
    private String address;
    private int capacity;
    private Date date;
    private Time hour;
    private int cost;
    private String district;

}
