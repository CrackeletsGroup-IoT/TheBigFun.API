package com.crackelets.bigfun.platform.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
public class StripeTokenDto {

    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    @Nullable
    private String token;
    private String username;
    @Nullable
    private boolean success;
}
