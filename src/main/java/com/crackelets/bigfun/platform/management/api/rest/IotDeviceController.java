package com.crackelets.bigfun.platform.management.api.rest;

import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.mapping.IotDeviceMapper;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResource;
import com.crackelets.bigfun.platform.management.service.IoTDeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/iot-devices")
public class IotDeviceController {

    private final IoTDeviceService ioTDeviceService;
    private final IotDeviceMapper ioTDeviceMapper;

    public IotDeviceController(IoTDeviceService ioTDeviceService, IotDeviceMapper ioTDeviceMapper) {
        this.ioTDeviceService = ioTDeviceService;
        this.ioTDeviceMapper = ioTDeviceMapper;
    }



    @GetMapping
    public ResponseEntity<List<IoTDeviceResource>> getAllDevices() {
        List<IoTDevice> devices = ioTDeviceService.getAllIoTDevices();
        List<IoTDeviceResource> deviceResources = devices.stream()
                .map(ioTDeviceMapper::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(deviceResources);
    }

    @GetMapping("/iotDevice/{id}")
    public ResponseEntity<IoTDeviceResource> getDeviceById(@PathVariable Long id) {
        IoTDevice device = ioTDeviceService.getIoTDeviceById(id);
        if (device == null) {
            return ResponseEntity.notFound().build();
        }
        IoTDeviceResource deviceResource = ioTDeviceMapper.toResource(device);
        return ResponseEntity.ok(deviceResource);
    }

    @PostMapping
    public ResponseEntity<IoTDevice> createIoTDevice(@RequestBody IoTDevice iotDevice) {
        IoTDevice createdIoTDevice = ioTDeviceService.createIoTDevice(iotDevice);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIoTDevice);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IoTDevice> updateIoTDevice(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        IoTDevice updatedIoTDevice = ioTDeviceService.updateIoTDevice(id, updates);
        return ResponseEntity.ok(updatedIoTDevice);
    }



}
