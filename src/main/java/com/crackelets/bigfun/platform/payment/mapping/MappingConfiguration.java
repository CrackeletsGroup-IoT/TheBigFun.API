package com.crackelets.bigfun.platform.payment.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("paymentMappingConfiguration")
public class MappingConfiguration {
    @Bean
    public PaymentMapper paymentMapper() {
        return new PaymentMapper();
    }

    @Bean
    public StripePaymentMapper stripePaymentMapper() {
        return new StripePaymentMapper();
    }

    @Bean
    public StripeChargeMapper stripeChargeMapper() {
        return new StripeChargeMapper();
    }
}
