package com.crackelets.bigfun.platform.profile.service;

import com.crackelets.bigfun.platform.profile.domain.model.Organizer;
import com.crackelets.bigfun.platform.profile.domain.persistence.OrganizerRepository;
import com.crackelets.bigfun.platform.profile.domain.service.OrganizerService;
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
public class OrganizerServiceImpl implements OrganizerService {

    private static final String ENTITY="Organizer";
    private final OrganizerRepository organizerRepository;
    private final Validator validator;

    public OrganizerServiceImpl(OrganizerRepository organizerRepository, Validator validator) {
        this.organizerRepository = organizerRepository;
        this.validator = validator;
    }


    @Override
    public List<Organizer> getAll() {
        return organizerRepository.findAll();
    }

    @Override
    public Page<Organizer> getAll(Pageable pageable) {
        return organizerRepository.findAll(pageable);
    }

    @Override
    public Organizer getById(Long organizerId) {
        return organizerRepository.findById(organizerId).orElseThrow(()->new ResourceNotFoundException(ENTITY,organizerId));
    }

    @Override
    public Organizer getByName(String mame) {
        var organizerWithName = organizerRepository.findFirstByName(mame);
        if(organizerWithName==null)
            throw new ResourceValidationException(ENTITY,"The organizer doesn't exist.");
        return organizerWithName;
    }

    @Override
    //diferente nombre / diferente correo / diferente nombre de usuario
    public Organizer create(Organizer organizer) {

        //Constraints validation
        Set<ConstraintViolation<Organizer>> violations=validator.validate(organizer);

        if(!violations.isEmpty()){ //si hay alguna violacion
            throw new ResourceValidationException(ENTITY,violations);
        }

        Organizer organizerWithUserName=organizerRepository.findFirstByUserName(organizer.getUserName());
        Organizer organizerWithEmail=organizerRepository.findFirstByEmail(organizer.getEmail());

        if(organizerWithUserName !=null && organizerWithEmail !=null){
            throw  new ResourceValidationException(ENTITY, "An organizer with the same user name and email already exists");
        }

        //Perform creation operation
        return organizerRepository.save(organizer);
    }

    @Override
    public Organizer update(Long organizerId, Organizer organizer) {

        //Constraints validation
        Set<ConstraintViolation<Organizer>> violations = validator.validate(organizer);

        if (!violations.isEmpty()) { //si hay alguna violacion
            throw new ResourceValidationException(ENTITY, violations);
        }

        //Name uniqueness validation
        Organizer organizerWithName = organizerRepository.findFirstByName(organizer.getName());
        Organizer organizerWithUserName=organizerRepository.findFirstByUserName(organizer.getUserName());
        Organizer organizerWithEmail=organizerRepository.findFirstByEmail(organizer.getEmail());

        //si el nombre del organizador existe && es falso que el id de este organizador es igual al id del parametro
        if (organizerWithName != null && !organizerWithName.getId().equals(organizer.getId())) {
            throw new ResourceValidationException(ENTITY, "An organizer with tne same name alredy exists");
        }
        if (organizerWithUserName != null && !organizerWithUserName.getId().equals(organizer.getId())) {
            throw new ResourceValidationException(ENTITY, "An organizer with tne same user name alredy exists");
        }

        //Perform creation operation

          //puede modificar el username
        return organizerRepository.findById(organizerId).map(organizerToUpdate -> organizerRepository.save(
                        organizerToUpdate.withName(organizer.getName())
                                .withUserName(organizer.getUserName())
                ))
                .orElseThrow(()->new ResourceNotFoundException(ENTITY,organizerId));
    }

    @Override
    public ResponseEntity<?> delete(Long organizerId) {

        return organizerRepository.findById(organizerId).map(organizer -> {

                    organizerRepository.delete(organizer);
                    return ResponseEntity.ok().build();})
                .orElseThrow(()->new ResourceNotFoundException(ENTITY,organizerId));
    }
}
