package com.crackelets.bigfun.platform.management.mapping;


import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResource;
import com.crackelets.bigfun.platform.shared.mapping.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class IotDeviceMapper implements Serializable {

    @Autowired
    private EnhancedModelMapper mapper;

    public IoTDeviceResource toResource(IoTDevice model){
        return mapper.map(model, IoTDeviceResource.class);
    }

    public IoTDevice toModel(IoTDeviceResource resource){
        return mapper.map(resource, IoTDevice.class);
    }
}
