package com.crackelets.bigfun.platform.booking.api.rest;


import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.service.EventService;
import com.crackelets.bigfun.platform.booking.mapping.EventMapper;
import com.crackelets.bigfun.platform.booking.resource.CreateEventResource;
import com.crackelets.bigfun.platform.booking.resource.EventResource;
import com.crackelets.bigfun.platform.booking.resource.UpdateEventResource;
import com.crackelets.bigfun.platform.shared.services.media.StorageService;
import com.crackelets.bigfun.platform.storage.service.MyFileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value= "/api/v1/events")
public class EventsController {

    private  final EventService eventService;

    private final EventMapper mapper;

    private final StorageService storageService;
    private final HttpServletRequest request;
    private final MyFileService myFileService;


    public EventsController(EventService eventService, EventMapper mapper, StorageService storageService, HttpServletRequest request, MyFileService myFileService) {
        this.eventService = eventService;
        this.mapper = mapper;
        this.storageService = storageService;
        this.request = request;
      this.myFileService = myFileService;
    }

    @Operation(summary = "Get all events")
    @GetMapping
    public Page<EventResource> getAllEvents(Pageable pageable){
        return mapper.modelListPage(eventService.getAll(), pageable);
    }

    @Operation(summary = "Get a event by id")
    @GetMapping("{eventId}")
    public EventResource getEventById(@PathVariable Long eventId){
        return mapper.toResource(eventService.getById(eventId));
    }


    @Operation(summary = "Post a event")
    @PostMapping("{organizerId}")
    public ResponseEntity<EventResource> createEvent(@RequestBody CreateEventResource resource, @PathVariable Long organizerId){
        return new ResponseEntity<>(mapper.toResource(eventService.create(mapper.toModel(resource), organizerId)), HttpStatus.CREATED);
    }

    @Operation(summary = "Edit a event")
    @PutMapping("{eventId}")
    public EventResource updateEvent(@PathVariable Long eventId,
                                     @RequestBody UpdateEventResource resource){
        return mapper.toResource(eventService.update(eventId, mapper.toModel(resource)));
    }

    @Operation(summary = "Get all users of a organizer")
    @GetMapping("/organizer/{organizerId}") // New endpoint
    public ResponseEntity<Page<EventResource>> getEventsByOrganizerId(@PathVariable Long organizerId, Pageable pageable) {
        List<Event> events = eventService.getAllByOrganizerId(organizerId);
        return new ResponseEntity<>(mapper.modelListPage(events, pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get a event")
    @DeleteMapping("{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId){ return eventService.delete(eventId);}

    @Operation(summary = "Post image of a event")
    @PostMapping("{eventId}/upload")
    public ResponseEntity<Event> uploadFiles(@PathVariable Long eventId, @RequestParam("file") MultipartFile file) throws IOException {

        Event post = eventService.getById(eventId);
        if (post == null) return ResponseEntity.notFound().build();
        String stringUrl = myFileService.uploadFile(file, "event" +eventId+ "image" + file.getOriginalFilename(), "the-big-fun-files");
        post.setImageUrl(stringUrl);
        Event postWithImages= eventService.update(eventId, post);
        return ResponseEntity.ok(postWithImages);
    }



}
