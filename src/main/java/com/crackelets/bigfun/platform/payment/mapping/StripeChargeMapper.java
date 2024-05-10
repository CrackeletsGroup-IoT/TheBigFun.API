package com.crackelets.bigfun.platform.payment.mapping;

import com.crackelets.bigfun.platform.payment.domain.model.StripeChargeDto;
import com.crackelets.bigfun.platform.payment.domain.model.StripeTokenDto;
import com.crackelets.bigfun.platform.payment.resource.CreateStripeChargeResource;
import com.crackelets.bigfun.platform.payment.resource.CreateStripeTokenResource;
import com.crackelets.bigfun.platform.payment.resource.StripeChargeResource;
import com.crackelets.bigfun.platform.payment.resource.StripeTokenResource;
import com.crackelets.bigfun.platform.shared.mapping.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class StripeChargeMapper {
    @Autowired
    private EnhancedModelMapper mapper;

    public StripeChargeResource toResource(StripeChargeDto model){
        return mapper.map(model, StripeChargeResource.class);
    }

    public StripeChargeDto toModel(CreateStripeChargeResource resource){
        return mapper.map(resource, StripeChargeDto.class);
    }
}
