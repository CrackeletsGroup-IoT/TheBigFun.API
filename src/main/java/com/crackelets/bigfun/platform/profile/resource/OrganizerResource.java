package com.crackelets.bigfun.platform.profile.resource;

import com.crackelets.bigfun.platform.booking.resource.EventResource;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor

public class OrganizerResource {

    private Long id;
    private String userName;
    private String name;
    private String email;
    private Set<EventResource> events;
}
