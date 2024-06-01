package com.crackelets.bigfun.platform.profile.api.rest;

import com.crackelets.bigfun.platform.booking.mapping.EventMapper;
import com.crackelets.bigfun.platform.profile.domain.service.OrganizerService;
import com.crackelets.bigfun.platform.profile.mapping.OrganizerMapper;
import com.crackelets.bigfun.platform.profile.resource.CreateOrganizerResource;
import com.crackelets.bigfun.platform.profile.resource.OrganizerResource;
import com.crackelets.bigfun.platform.profile.resource.UpdateOrganizerResource;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/v1/organizers", produces="application/json")
public class OrganizersController {

    private OrganizerService organizerService;
    private OrganizerMapper organizerMapper;
    private EventMapper eventMapper;


    public OrganizersController(OrganizerService organizerService, OrganizerMapper mapper1, EventMapper mapper2) {
        this.organizerService = organizerService;
        this.organizerMapper = mapper1;
        this.eventMapper = mapper2;

    }

    @Operation(summary = "Get all organizers")
    @GetMapping
    public Page<OrganizerResource> getAllOrganizers(Pageable pageable){
        return organizerMapper.modelListPage(organizerService.getAll(),pageable);
    }

    @Operation(summary = "Get a organizer by username")
    @GetMapping("byname/{name}")
    public OrganizerResource getByName(@PathVariable String name){
        return organizerMapper.toResource(organizerService.getByName(name));
    }


    @Operation(summary = "Get a organizer by id")
    @GetMapping("{organizerId}")          //"PathVariable": reconoce la variable de esta linea
    public OrganizerResource getOrganizerById(@PathVariable Long organizerId){
        return organizerMapper.toResource(organizerService.getById(organizerId));
    }

    @Operation(summary = "Create a organizer")
    @PostMapping
    public ResponseEntity<OrganizerResource> createOrganizer(@RequestBody CreateOrganizerResource resource){

        return new ResponseEntity<>(organizerMapper.toResource(organizerService.create(organizerMapper.toModel(resource))), HttpStatus.CREATED);
    }

    @Operation(summary = "Edit a organizer")
    @PutMapping("{organizerId}")
    public OrganizerResource updateOrganizer(@PathVariable Long organizerId,
                                         @RequestBody UpdateOrganizerResource resource){
        return organizerMapper.toResource(organizerService.update(organizerId,organizerMapper.toModel(resource)));
    }

    @Operation(summary = "Delete a organizer")
    @DeleteMapping("{organizerId}")
    public ResponseEntity<?> deleteOrganizer(@PathVariable Long organizerId){
        return organizerService.delete(organizerId);
    }

}
