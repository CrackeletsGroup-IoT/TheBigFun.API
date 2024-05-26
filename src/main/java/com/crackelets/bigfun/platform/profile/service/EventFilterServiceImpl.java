package com.crackelets.bigfun.platform.profile.service;

import com.crackelets.bigfun.platform.booking.api.internal.BookingContextFacade;

public class EventFilterServiceImpl {
    private final BookingContextFacade bookingContextFacade;

    public EventFilterServiceImpl(BookingContextFacade bookingContextFacade) {
        this.bookingContextFacade = bookingContextFacade;
    }


/*    @Override
    public List<Event> getAllEventsByOrganizer(Long id) {
        return bookingContextFacade.getAllByOrganizerId(id);
    }*/
}
