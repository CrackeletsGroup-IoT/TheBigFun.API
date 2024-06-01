package com.crackelets.bigfun.platform.profile.api.rest;

import com.crackelets.bigfun.platform.profile.domain.service.AttendeeService;
import com.crackelets.bigfun.platform.profile.mapping.AttendeeMapper;
import com.crackelets.bigfun.platform.profile.resource.AttendeeResource;
import com.crackelets.bigfun.platform.profile.resource.CreateAttendeeResource;
import com.crackelets.bigfun.platform.profile.resource.UpdateAttendeeResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attendees")
@Tag(name = "Attendees", description = "Create,read, update and delete attendees")
public class AttendeesController {

    private final AttendeeService attendeeService;
    private final AttendeeMapper mapper;

    public AttendeesController(AttendeeService attendeeService, AttendeeMapper mapper) {
        this.attendeeService = attendeeService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get attendees", description = "Get All attendees.")
    @GetMapping
    public Page<AttendeeResource> getAllAttendees(Pageable pageable) {
        return mapper.modelListPage(attendeeService.getAll(), pageable);
    }

    @Operation(summary = "Get a attendee by id")
    @GetMapping("{attendeeId}")
    public AttendeeResource getAttendeeById(@PathVariable Long attendeeId) {
        return mapper.toResource(attendeeService.getById(attendeeId));
    }

    @Operation(summary = "Get a attendee by username")
    @GetMapping("byname/{name}")
    public AttendeeResource getByName(@PathVariable String name){
        return mapper.toResource(attendeeService.getByName(name));
    }

    @Operation(summary = "Create attendees", responses = {
            @ApiResponse(description = "Attendee successfully created",responseCode = "201",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = AttendeeResource.class)))
    })
    @PostMapping  // To create
    public ResponseEntity<AttendeeResource> createAttendee(@RequestBody CreateAttendeeResource resource) {
        return new ResponseEntity<>(mapper.toResource(attendeeService.create(mapper.toModel(resource))), HttpStatus.CREATED);
    }

    @Operation(summary = "Edit a attendee by id")
    @PutMapping("{attendeeId}")  // Update
    public AttendeeResource updateAttendee(@PathVariable Long attendeeId, @RequestBody UpdateAttendeeResource resource) {
        return mapper.toResource(attendeeService.update(attendeeId, mapper.toModel(resource)));
    }

    @Operation(summary = "Delete a attendee by id")
    @DeleteMapping("{attendeeId}")
    public ResponseEntity<?> deleteAttendee(@PathVariable Long attendeeId) {
        return attendeeService.delete(attendeeId);
    }

}
