package com.crackelets.bigfun.platform.payment.mapping;

import com.crackelets.bigfun.platform.payment.domain.model.Payment;
import com.crackelets.bigfun.platform.payment.domain.model.StripeTokenDto;
import com.crackelets.bigfun.platform.payment.resource.CreatePaymentResource;
import com.crackelets.bigfun.platform.payment.resource.CreateStripeTokenResource;
import com.crackelets.bigfun.platform.payment.resource.PaymentResource;
import com.crackelets.bigfun.platform.payment.resource.StripeTokenResource;
import com.crackelets.bigfun.platform.shared.mapping.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class StripePaymentMapper implements Serializable {

    @Autowired
    private EnhancedModelMapper mapper;

    public StripeTokenResource toResource(StripeTokenDto model){
        return mapper.map(model, StripeTokenResource.class);
    }

    public StripeTokenDto toModel(CreateStripeTokenResource resource){
        return mapper.map(resource, StripeTokenDto.class);
    }
}
