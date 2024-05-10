package com.crackelets.bigfun.platform.payment.domain.service;

import com.crackelets.bigfun.platform.payment.domain.model.StripeChargeDto;
import com.crackelets.bigfun.platform.payment.domain.model.StripeTokenDto;

public interface StripePaymentService {
    void init();
    StripeTokenDto createCardToken(StripeTokenDto model);
    StripeChargeDto charge(StripeChargeDto chargeRequest);
}
