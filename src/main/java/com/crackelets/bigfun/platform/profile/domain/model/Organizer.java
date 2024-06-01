package com.crackelets.bigfun.platform.profile.domain.model;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.shared.domain.model.AuditModel;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organizers")

public class Organizer extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Autogenerate value
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String userName;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String email;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER,mappedBy="organizer", orphanRemoval = true)
    private Set<Event> events = new HashSet<>();



}
