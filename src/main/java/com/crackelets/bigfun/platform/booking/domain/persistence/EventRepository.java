package com.crackelets.bigfun.platform.booking.domain.persistence;


import com.crackelets.bigfun.platform.booking.domain.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByName(String name);
    List<Event> findAllByOrganizerId(Long organizerId);
}
