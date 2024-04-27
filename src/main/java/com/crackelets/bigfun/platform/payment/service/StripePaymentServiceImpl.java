package com.crackelets.bigfun.platform.payment.service;

import com.crackelets.bigfun.platform.payment.domain.model.StripeChargeDto;
import com.crackelets.bigfun.platform.payment.domain.model.StripeTokenDto;
import com.crackelets.bigfun.platform.payment.domain.service.StripePaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;



@Slf4j
@Service
public class StripePaymentServiceImpl implements StripePaymentService {

    private final String apiKey = "pk_test_51PA1ZlKKAQPi64qcn8nkMW2hLADxxlMcPrJhoadgr2fdHVttbOftQYrcCmT7TU5dnd1Y8BAYyyku1lufBjJY5JhO00aNumyrP9";
    private final String secretApiKey = "sk_test_51PA1ZlKKAQPi64qcYCemnQudTCpMHUOuFhAjted6X7dvBBm5Y2kQfNcE6VSGftfGA94cDLKI2UYFv3tHwJCoDYU900KmmbBUtH";
    @Override
    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    @Override
    public StripeTokenDto createCardToken(StripeTokenDto model) {
        Stripe.apiKey = apiKey;
        try {
            Map<String, Object> card = new HashMap<>();
            card.put("number", model.getCardNumber());
            card.put("exp_month", Integer.parseInt(model.getExpMonth()));
            card.put("exp_year", Integer.parseInt(model.getExpYear()));
            card.put("cvc", model.getCvc());

            Map<String, Object> params = new HashMap<>();
            params.put("card", card);

            Token token = Token.create(params);
            System.out.println("Token creado: " + token.getId());

            if (token != null && token.getId() != null) {
                model.setSuccess(true);
                model.setToken(token.getId());
            }

            return model;
        } catch (StripeException e) {
            log.error("Error al crear el token de la tarjeta con Stripe", e);
            throw new RuntimeException("Error al crear el token de la tarjeta con Stripe: " + e.getMessage(), e);
        }
    }

    @Override
    public StripeChargeDto charge(StripeChargeDto chargeRequest) {
        Stripe.apiKey = secretApiKey;
        try {
            chargeRequest.setSuccess(false);
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100));
            chargeParams.put("currency", "USD");
            chargeParams.put("description", "Payment for id " + chargeRequest.getAdditionalInfo().getOrDefault("ID_TAG", ""));
            chargeParams.put("source", chargeRequest.getStripeToken());
            Map<String, Object> metaData = new HashMap<>();
            metaData.put("id", chargeRequest.getChargeId());
            metaData.putAll(chargeRequest.getAdditionalInfo());
            chargeParams.put("metadata", metaData);
            Charge charge = Charge.create(chargeParams);
            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());

            if (charge.getPaid()) {
                chargeRequest.setChargeId(charge.getId());
                chargeRequest.setSuccess(true);

            }
            return chargeRequest;
        } catch (StripeException e) {
            log.error("StripeService (charge)", e);
            throw new RuntimeException(e.getMessage());
        }


    }
}
