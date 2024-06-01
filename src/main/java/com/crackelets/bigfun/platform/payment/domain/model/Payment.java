package com.crackelets.bigfun.platform.payment.domain.model;


import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import com.crackelets.bigfun.platform.shared.domain.model.AuditModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    private String uuid;

    private String qrImg;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private EventAttendee eventAttendee;

}