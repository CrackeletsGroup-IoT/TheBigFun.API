package com.crackelets.bigfun.platform.management.service;

import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventAttendeeRepository;
import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.IotDeviceRepository;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResource;
import com.crackelets.bigfun.platform.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;

@Service
public class IoTDeviceServiceImpl implements IoTDeviceService{


    private final IotDeviceRepository ioTDeviceRepository;
    private final EventAttendeeRepository eventAttendeeRepository;

    public IoTDeviceServiceImpl(IotDeviceRepository ioTDeviceRepository,
                                EventAttendeeRepository eventAttendeeRepository) {
        this.ioTDeviceRepository = ioTDeviceRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
    }

    @Override
    public List<IoTDevice> getAllIoTDevices() {
        return ioTDeviceRepository.findAll();

    }

    @Override
    public IoTDevice getIoTDeviceById(Long id) {
        return ioTDeviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IoTDevice", id));

    }

    @Override
    public IoTDevice createIoTDevice(IoTDevice ioTDevice) {
        return ioTDeviceRepository.save(ioTDevice);
    }

    @Override
    public IoTDevice updateIoTDeviceDistance(Long id, IoTDevice ioTDevice) {
        IoTDevice existingIoTDevice = ioTDeviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IoTDevice", id));
        existingIoTDevice.setDistanceP1(ioTDevice.getDistanceP1());
        existingIoTDevice.setDistanceP2(ioTDevice.getDistanceP2());
        existingIoTDevice.setDistanceBetween(ioTDevice.getDistanceBetween());
        return ioTDeviceRepository.save(existingIoTDevice);

    }

    @Override
    public IoTDevice updateIoTDevicePulse(Long id, IoTDevice ioTDevice) {
        IoTDevice existingIoTDevice = ioTDeviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IoTDevice", id));
        existingIoTDevice.setPulse(ioTDevice.getPulse());
        existingIoTDevice.setSafe(ioTDevice.isSafe());
        existingIoTDevice.setDanger(ioTDevice.isDanger());
        return ioTDeviceRepository.save(existingIoTDevice);

    }


}
