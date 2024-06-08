package com.crackelets.bigfun.platform.management.api.rest;

import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.mapping.IoTDeviceDistanceMapper;
import com.crackelets.bigfun.platform.management.mapping.IoTDevicePulseMapper;
import com.crackelets.bigfun.platform.management.mapping.IotDeviceMapper;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResource;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResourceDistance;
import com.crackelets.bigfun.platform.management.resource.IoTDeviceResourcePulse;
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
    private final IoTDeviceDistanceMapper ioTDeviceDistanceMapper;
    private final IoTDevicePulseMapper ioTDevicePulseMapper;

    public IotDeviceController(IoTDeviceService ioTDeviceService, IotDeviceMapper ioTDeviceMapper, IoTDeviceDistanceMapper ioTDeviceDistanceMapper, IoTDevicePulseMapper ioTDevicePulseMapper) {
        this.ioTDeviceService = ioTDeviceService;
        this.ioTDeviceMapper = ioTDeviceMapper;
        this.ioTDeviceDistanceMapper = ioTDeviceDistanceMapper;
        this.ioTDevicePulseMapper = ioTDevicePulseMapper;
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
    public ResponseEntity<IoTDeviceResource> createIoTDevice(@RequestBody IoTDeviceResource iotDevice) {
        IoTDevice createdIoTDevice = ioTDeviceService.createIoTDevice(ioTDeviceMapper.toModel(iotDevice));
        return ResponseEntity.status(HttpStatus.CREATED).body(ioTDeviceMapper.toResource(createdIoTDevice));
    }

    //UpdateIotDeviceDistance
    @PutMapping("/distance/{id}")
    public ResponseEntity<IoTDeviceResourceDistance> updateIoTDeviceDistance(@PathVariable Long id, @RequestBody IoTDeviceResourceDistance iotDevice) {
        IoTDevice updatedIoTDevice = ioTDeviceService.updateIoTDeviceDistance(id, ioTDeviceDistanceMapper.toModel(iotDevice));
        return ResponseEntity.ok(ioTDeviceDistanceMapper.toResource(updatedIoTDevice));
    }



    //UpdateIotDevicePulse
    @PutMapping("/pulse/{id}")
    public ResponseEntity<IoTDeviceResourcePulse> updateIoTDevicePulse(@PathVariable Long id, @RequestBody IoTDeviceResourcePulse iotDevice) {
        IoTDevice updatedIoTDevice = ioTDeviceService.updateIoTDevicePulse(id, ioTDevicePulseMapper.toModel(iotDevice));
        return ResponseEntity.ok(ioTDevicePulseMapper.toResource(updatedIoTDevice));
    }






}
