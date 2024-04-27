package com.crackelets.bigfun.platform.payment.api.rest;

import com.crackelets.bigfun.platform.payment.domain.model.StripeChargeDto;
import com.crackelets.bigfun.platform.payment.domain.model.StripeTokenDto;
import com.crackelets.bigfun.platform.payment.domain.service.StripePaymentService;
import com.crackelets.bigfun.platform.payment.mapping.StripePaymentMapper;
import com.crackelets.bigfun.platform.payment.resource.CreateStripeTokenResource;
import com.stripe.Stripe;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/stripe-payment", produces = "application/json")
@Tag(name = "Stripe", description = "Pay using Stripe")
public class StripePaymentController {

    private final StripePaymentService stripePaymentService;
    private final StripePaymentMapper mapper;

    public StripePaymentController(StripePaymentService stripePaymentService, StripePaymentMapper mapper) {
        this.stripePaymentService = stripePaymentService;
        this.mapper = mapper;
    }

    @PostMapping("/card/token")
    public StripeTokenDto createCardToken(@RequestBody CreateStripeTokenResource model) {

        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(model);
        return stripePaymentService.createCardToken(mapper.toModel(model));
    }

    @PostMapping("/charge")
    public StripeChargeDto charge(@RequestBody StripeChargeDto model) {

        return stripePaymentService.charge(model);
    }
}
