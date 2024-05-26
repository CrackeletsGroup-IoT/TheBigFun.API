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
    private Set<EventAttendee> eventAttendees;

    public EventResource(Event event) {


        this.id = event.getId();
        this.name = event.getName();
        this.address = event.getAddress();
        this.capacity = event.getCapacity();
        this.date = event.getDate();
        this.cost = event.getCost();

        String postImageUrl = event.getImageUrl();
        if (postImageUrl != null) {
            this.imageUrl = postImageUrl;
        } else {
            this.imageUrl ="https://images.pexels.com/photos/1396122/pexels-photo-1396122.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
        }

        this.district = event.getDistrict();
    }




}
