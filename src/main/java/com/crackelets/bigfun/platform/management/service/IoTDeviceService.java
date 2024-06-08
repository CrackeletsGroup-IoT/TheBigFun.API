package com.crackelets.bigfun.platform.management.service;

import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResource;

import java.util.List;
import java.util.Map;

public interface IoTDeviceService {

    IoTDevice createIoTDevice(IoTDevice ioTDevice);


    IoTDevice updateIoTDeviceDistance(Long id, IoTDevice ioTDevice);

    IoTDevice updateIoTDevicePulse(Long id, IoTDevice ioTDevice);

    List<IoTDevice> getAllIoTDevices();


    IoTDevice getIoTDeviceById(Long id);
}
