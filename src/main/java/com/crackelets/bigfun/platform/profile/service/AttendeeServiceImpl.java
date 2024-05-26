package com.crackelets.bigfun.platform.profile.service;

import com.crackelets.bigfun.platform.profile.domain.model.Attendee;
import com.crackelets.bigfun.platform.profile.domain.persistence.AttendeeRepository;
import com.crackelets.bigfun.platform.profile.domain.service.AttendeeService;
import com.crackelets.bigfun.platform.shared.exception.ResourceNotFoundException;
import com.crackelets.bigfun.platform.shared.exception.ResourceValidationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;


@Service
public class AttendeeServiceImpl implements AttendeeService {

    private static final String ENTITY = "Attendee";
    private final AttendeeRepository attendeeRepository;
    private final Validator validator;

    public AttendeeServiceImpl(AttendeeRepository attendeeRepository, Validator validator) {
        this.attendeeRepository = attendeeRepository;
        this.validator = validator;
    }

    @Override
    public List<Attendee> getAll() {
        return attendeeRepository.findAll();
    }

    @Override
    public Page<Attendee> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public Attendee getById(Long attendeeId) {
        return attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, attendeeId));
    }

    @Override
    public Attendee getByName(String name) {
        var attendeeWithName = attendeeRepository.findByUserName(name);
        if(attendeeWithName==null)
            throw new ResourceValidationException(ENTITY,"The attendee doesn't exist.");
        return attendeeWithName;
    }

    @Override
    public Attendee create(Attendee attendee) {

        Set<ConstraintViolation<Attendee>> violations = validator.validate(attendee);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        Attendee attendeeWithUserName = attendeeRepository.findByUserName(attendee.getUserName());

        if(attendeeWithUserName != null)
            throw new ResourceValidationException(ENTITY,
                    "An attendee with the same user name already exists.");

        return attendeeRepository.save(attendee);
    }

    @Override
    public Attendee update(Long id, Attendee attendee) {

        Set<ConstraintViolation<Attendee>> violations = validator.validate(attendee);

        if(!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        Attendee attendeeWithUserName = attendeeRepository.findByUserName(attendee.getUserName());

        if(attendeeWithUserName != null && !attendeeWithUserName.getId().equals(attendee.getId()))
            throw new ResourceValidationException(ENTITY,
                    "An attendee with the same username already exists.");

        return attendeeRepository.findById(id).map(attendeeToUpdate ->
                        attendeeRepository.save(
                                attendeeToUpdate.withName(attendee.getName())
                                        .withEmail(attendee.getEmail())
                                        .withUserName(attendee.getUserName())))
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, id));

    }

    @Override
    public ResponseEntity<?> delete(Long attendeeId) {

        return attendeeRepository.findById(attendeeId).map(attendee -> {
                    attendeeRepository.delete(attendee);
                    return ResponseEntity.ok().build();})
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, attendeeId));
    }

    @Override
    public Attendee addEventToAttendee(Long attendeeId, String eventName) {

        return null;

    }
}
