package com.crackelets.bigfun.platform.management.mapping;

import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResourceDistance;
import com.crackelets.bigfun.platform.shared.mapping.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class IoTDeviceDistanceMapper implements Serializable {

    @Autowired
    private EnhancedModelMapper mapper;

    public IoTDeviceResourceDistance toResource(IoTDevice model){
        return mapper.map(model, IoTDeviceResourceDistance.class);
    }

    public IoTDevice toModel(IoTDeviceResourceDistance resource){
        return mapper.map(resource, IoTDevice.class);
    }


}
