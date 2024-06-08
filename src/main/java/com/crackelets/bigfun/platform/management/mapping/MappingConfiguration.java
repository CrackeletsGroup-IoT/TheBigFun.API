package com.crackelets.bigfun.platform.management.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("managementMappingConfiguration")
public class MappingConfiguration {

    @Bean
    public IotDeviceMapper iotDeviceMapper() {
        return new IotDeviceMapper();
    }

    @Bean
    public IoTDeviceDistanceMapper ioTDeviceDistanceMapper(){
        return new IoTDeviceDistanceMapper();
    }

    @Bean
    public IoTDevicePulseMapper ioTDevicePulseMapper(){
        return new IoTDevicePulseMapper();
    }
}
