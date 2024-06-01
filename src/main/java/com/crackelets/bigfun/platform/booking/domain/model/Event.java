package com.crackelets.bigfun.platform.booking.domain.model;

import com.crackelets.bigfun.platform.profile.domain.model.Organizer;
import com.crackelets.bigfun.platform.shared.domain.model.AuditModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@Entity
@Table(name = "events")
public class Event extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String name;

    @Size(max =240)
    private String address;

    @NotNull
    private int capacity;

    //@ElementCollection
    //@CollectionTable(name = "event_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "image_url")
    private String imageUrl;


    @NotNull
    private String date;

    @NotNull
    private int cost;

    @Size(max = 50)
    @NotNull
    private String district;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "event")
    private Set<EventAttendee> attendeesListByEvent;

    public Event addAttendee(Event event,Long attendeeId){

        if(attendeesListByEvent ==null) attendeesListByEvent = new HashSet<>();

        this.attendeesListByEvent.add(new EventAttendee(this, attendeeId));

        return this;
    }

}
