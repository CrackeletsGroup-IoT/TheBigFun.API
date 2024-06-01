package com.crackelets.bigfun.platform.booking.service;

import com.crackelets.bigfun.platform.booking.api.internal.BookingContextFacade;
import com.crackelets.bigfun.platform.booking.domain.model.Event;
import com.crackelets.bigfun.platform.booking.domain.service.EventService;

import java.util.List;

public class BookingContextFacadeImpl implements BookingContextFacade {
    private final EventService eventService;

    public BookingContextFacadeImpl(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventService.getAll();
    }

}
