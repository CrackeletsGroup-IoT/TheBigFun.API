package com.crackelets.bigfun.platform.booking.api.rest;


import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.service.EventService;
import com.crackelets.bigfun.platform.booking.mapping.EventMapper;
import com.crackelets.bigfun.platform.booking.resource.CreateEventResource;
import com.crackelets.bigfun.platform.booking.resource.EventResource;
import com.crackelets.bigfun.platform.booking.resource.UpdateEventResource;
import com.crackelets.bigfun.platform.shared.services.media.StorageService;
import com.crackelets.bigfun.platform.storage.service.MyFileService;
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

    @GetMapping
    public Page<EventResource> getAllEvents(Pageable pageable){
        return mapper.modelListPage(eventService.getAll(), pageable);
    }

    @GetMapping("{eventId}")
    public EventResource getEventById(@PathVariable Long eventId){
        return mapper.toResource(eventService.getById(eventId));
    }


    @PostMapping
    public ResponseEntity<EventResource> createEvent(@RequestBody CreateEventResource resource){
        return new ResponseEntity<>(mapper.toResource(eventService.create(mapper.toModel(resource))), HttpStatus.CREATED);
    }

    //@PostMapping()

    @PutMapping("{eventId}")
    public EventResource updateEvent(@PathVariable Long eventId,
                                     @RequestBody UpdateEventResource resource){
        return mapper.toResource(eventService.update(eventId, mapper.toModel(resource)));
    }

/*    @GetMapping("org/{organizerId}")
    public Page<EventResource> getAllEventsByOrginerId(Pageable pageable,@PathVariable Long organizerId){
        return mapper.modelListPage(eventService.getAllByOrganizerId(organizerId), pageable);
    }*/

    @DeleteMapping("{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId){ return eventService.delete(eventId);}

    @PostMapping("{eventId}/upload")
    public ResponseEntity<Event> uploadFiles(@PathVariable Long eventId, @RequestParam("file") MultipartFile file) throws IOException {

        Event post = eventService.getById(eventId);

        if (post == null) return ResponseEntity.notFound().build();

        String path = storageService.storage(file);
        String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String url = ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("/api/v1/events/")
                    .path(path)
                    .toUriString();

        String stringUrl = myFileService.uploadFile(file, "event" +eventId+ "image" + file.getOriginalFilename(), "the-big-fun-files");
        post.setImageUrl(stringUrl);
        Event postWithImages= eventService.update(eventId, post);

        return ResponseEntity.ok(postWithImages);
    }

    /*
    @GetMapping("{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Resource file = storageService.loadAsResource(filename);
        String contentType = Files.probeContentType(file.getFile().toPath());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }*/



}
