package com.crackelets.bigfun.platform.management.service;

import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResource;

import java.util.List;
import java.util.Map;

public interface IoTDeviceService {

    IoTDevice createIoTDevice(IoTDevice ioTDevice);

    IoTDevice updateIoTDevice(Long id, Map<String, Object> updates);


    List<IoTDevice> getAllIoTDevices();


    IoTDevice getIoTDeviceById(Long id);
}
