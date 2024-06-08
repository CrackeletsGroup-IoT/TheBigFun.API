package com.crackelets.bigfun.platform.booking.service;

import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventAttendeeRepository;
import com.crackelets.bigfun.platform.booking.domain.persistence.EventRepository;
import com.crackelets.bigfun.platform.booking.domain.service.EventAttendeeService;
import com.crackelets.bigfun.platform.management.IoTDevice;
import com.crackelets.bigfun.platform.management.IotDeviceRepository;
import com.crackelets.bigfun.platform.profile.domain.model.Attendee;
import com.crackelets.bigfun.platform.profile.domain.persistence.AttendeeRepository;
import com.crackelets.bigfun.platform.shared.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventAttendeeServiceImpl implements EventAttendeeService {

    private final EventAttendeeRepository eventAttendeeRepository;

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;
    private final IotDeviceRepository iotDeviceRepository;

    private final Validator validator;

    public EventAttendeeServiceImpl(EventAttendeeRepository eventAttendeeRepository, EventRepository eventRepository, AttendeeRepository attendeeRepository, Validator validator, IotDeviceRepository iotDeviceRepository) {
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.eventRepository = eventRepository;
        this.attendeeRepository = attendeeRepository;
        this.validator = validator;
        this.iotDeviceRepository = iotDeviceRepository;
    }

    @Override
    public EventAttendee addIoTDevice(Long eventAttendeeId, Long ioTDeviceId) {
        Optional<EventAttendee> eventAttendee = eventAttendeeRepository.findEventAttendeeById(eventAttendeeId);
        if (!eventAttendee.isPresent()){
            throw new RuntimeException("EventAttendee not found with id " + eventAttendeeId);
        }
        Optional<IoTDevice> iotDevice = iotDeviceRepository.findById(ioTDeviceId);
        if (!iotDevice.isPresent()){
            throw new RuntimeException("IoTDevice not found with id " + ioTDeviceId);
        }
        Optional<EventAttendee> eventAttendeeWithIoTDevice = eventAttendeeRepository.findEventAttendeeByIoTDevice_Id(ioTDeviceId);
        if (eventAttendeeWithIoTDevice.isPresent()){
            throw new RuntimeException("IoTDevice already assigned to an EventAttendee");
        }

        EventAttendee eventAttendeeToSave = eventAttendee.get();
        eventAttendeeToSave.setIoTDevice(iotDevice.get());
        IoTDevice ioTDeviceToSave = iotDevice.get();
        ioTDeviceToSave.setEventAttendee(eventAttendeeToSave);
        iotDeviceRepository.save(ioTDeviceToSave);
        return eventAttendeeRepository.save(eventAttendeeToSave);

    }

    @Override
    public EventAttendee deleteIoTDevice(Long eventAttendeeId) {
        Optional<EventAttendee> eventAttendee = eventAttendeeRepository.findEventAttendeeById(eventAttendeeId);
        if (!eventAttendee.isPresent()){
            throw new RuntimeException("EventAttendee not found with id " + eventAttendeeId);
        }
        Optional<IoTDevice> iotDevice = iotDeviceRepository.findByEventAttendeeId(eventAttendeeId);
        if (eventAttendee.get().getIoTDevice() == null && iotDevice.isPresent()){
            throw new RuntimeException("EventAttendee does not have an IoTDevice");
        }
        EventAttendee eventAttendeeWithIoTDeviceToDelete = eventAttendee.get();
        IoTDevice ioTDeviceToDelete = eventAttendeeWithIoTDeviceToDelete.getIoTDevice();
        ioTDeviceToDelete.setEventAttendee(null);
        eventAttendeeWithIoTDeviceToDelete.setIoTDevice(null);
        iotDeviceRepository.save(ioTDeviceToDelete);
        return eventAttendeeRepository.save(eventAttendeeWithIoTDeviceToDelete);
    }



    @Override
    public List<EventAttendee> getAll() {
        return eventAttendeeRepository.findAll();
    }

    @Override
    public EventAttendee getEventAttendeeById(Long id) {
        return eventAttendeeRepository.findEventAttendeeById(id).orElseThrow(() -> new ResourceNotFoundException("EventAttendee", id));
    }

    @Override
    public EventAttendee create(Long eventId, Long attendeeId) {
        EventAttendee eventAttendee = new EventAttendee();
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (!optionalEvent.isPresent()){
            throw new RuntimeException("Event not found with id " + eventId);
        }
        Optional<Attendee> optionalAttendee = attendeeRepository.findById(attendeeId);

        if (!optionalAttendee.isPresent()){
            throw new RuntimeException("Attendee not found with id " + attendeeId);
        }

        Event event = optionalEvent.get();
        eventAttendee.setEvent(event);
        Attendee attendee = optionalAttendee.get();
        eventAttendee.setAttendee(attendee);
        return eventAttendeeRepository.save(eventAttendee);
    }

    @Override
    public ResponseEntity<?> delete(Long eventAttendeeId) {
        eventAttendeeRepository.deleteById(eventAttendeeId);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<Event> getAllByAttendeeId(Long attendeeId) {
        return eventAttendeeRepository.findAllByAttendee_Id(attendeeId).stream()
                .map(EventAttendee::getEvent)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventAttendee> getAllAttendeesByEventId(Long eventId) {
        return eventAttendeeRepository.findAllByEvent_Id(eventId);
    }
}
