package com.crackelets.bigfun.platform.payment.resource;

import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreateStripeChargeResource {
    private String  stripeToken;
    private String  username;
    private Double  amount;
}
