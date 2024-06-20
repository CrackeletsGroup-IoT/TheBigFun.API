package com.crackelets.bigfun.platform.management.mapping;

import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResourcePulse;
import com.crackelets.bigfun.platform.shared.mapping.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class IoTDevicePulseMapper implements Serializable {

    @Autowired
    private EnhancedModelMapper mapper;

    public IoTDeviceResourcePulse toResource(IoTDevice model){
        return mapper.map(model, IoTDeviceResourcePulse.class);
    }

    public IoTDevice toModel(IoTDeviceResourcePulse resource){
        return mapper.map(resource, IoTDevice.class);
    }


}
