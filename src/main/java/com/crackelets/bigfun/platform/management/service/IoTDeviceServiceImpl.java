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
    public IoTDevice updateIoTDevice(Long id, Map<String, Object> updates) {
        IoTDevice existingIoTDevice = ioTDeviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IoTDevice", id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "distanceP1":
                    existingIoTDevice.setDistanceP1((Double) value);
                    break;
                case "distanceP2":
                    existingIoTDevice.setDistanceP2((Double) value);
                    break;
                case "distanceBetween":
                    existingIoTDevice.setDistanceBetween((Double) value);
                    break;
                case "pulse":
                    existingIoTDevice.setPulse((Double) value);
                    break;
                case "safe":
                    existingIoTDevice.setSafe((Boolean) value);
                    break;
                case "danger":
                    existingIoTDevice.setDanger((Boolean) value);
                    break;
                case "time":
                    existingIoTDevice.setTime((String) value);
                    break;
                case "eventAttendee":
                    existingIoTDevice.setEventAttendee((EventAttendee) value);
                    break;

            }
        });

        return ioTDeviceRepository.save(existingIoTDevice);
    }
}
