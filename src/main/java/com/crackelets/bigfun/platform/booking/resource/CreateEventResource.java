package com.crackelets.bigfun.platform.booking.resource;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventResource {

    @NotNull
    @NotBlank
    @Size(max= 50)
    @Column(unique = true)
    private String name;

    @Size(max =240)
    private String address;

    @NotNull
    private int capacity;
    @NotNull
    private String date;

    @NotNull
    private String hour;

    @NotNull
    private String description;

    @NotNull
    private int cost;

    @Size(max = 50)
    @NotNull
    private String district;


}
