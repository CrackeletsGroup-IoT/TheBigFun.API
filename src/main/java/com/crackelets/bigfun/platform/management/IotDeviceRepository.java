package com.crackelets.bigfun.platform.management;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IotDeviceRepository extends JpaRepository<IoTDevice, Long> {
    Optional<IoTDevice> findByEventAttendeeId (long eventAttendeeId);
}
