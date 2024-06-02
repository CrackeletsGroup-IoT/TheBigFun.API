package com.crackelets.bigfun.platform.booking.mapping;

import com.crackelets.bigfun.platform.booking.domain.model.EventAttendee;
import com.crackelets.bigfun.platform.booking.resource.EventAttendeeResource;
import com.crackelets.bigfun.platform.shared.mapping.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class EventAttendeeMapper implements Serializable {

    @Autowired
    private EnhancedModelMapper mapper;

    public EventAttendeeResource toResource(EventAttendee model){
        return mapper.map(model,EventAttendeeResource.class); }

    public Page<EventAttendeeResource> modelListPage(List<EventAttendee> modelList, Pageable pageable){
        return new PageImpl<>(mapper.mapList(modelList,EventAttendeeResource.class),pageable,modelList.size());
    }

}
