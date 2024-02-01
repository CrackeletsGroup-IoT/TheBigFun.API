package com.crackelets.bigfun.platform.profile.service;

import com.crackelets.bigfun.platform.profile.api.internal.ProfileContextFacade;
import com.crackelets.bigfun.platform.profile.domain.model.Organizer;
import com.crackelets.bigfun.platform.profile.domain.service.OrganizerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileContextFacadeImpl implements ProfileContextFacade {

    private final OrganizerService organizerService;

    public ProfileContextFacadeImpl(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @Override
    public List<Organizer> getAllOrganizers() {
        return organizerService.getAll();
    }
}










