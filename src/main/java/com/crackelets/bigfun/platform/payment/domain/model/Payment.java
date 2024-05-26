package com.crackelets.bigfun.platform.payment.domain.model;


import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import com.crackelets.bigfun.platform.shared.domain.model.AuditModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;


import java.util.Date;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "payments")
public class Payment extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @NotNull
    @NotBlank
    private String uuid;

    @NotNull
    @NotBlank
    private String qrImg;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_attendee_id", nullable = false)
    private EventAttendee eventAttendee;

}