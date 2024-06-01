package com.crackelets.bigfun.platform.security.api;

import com.crackelets.bigfun.platform.security.domain.service.UserService;
import com.crackelets.bigfun.platform.security.domain.service.communication.AuthenticateRequest;
import com.crackelets.bigfun.platform.security.domain.service.communication.RegisterRequest;
import com.crackelets.bigfun.platform.security.mapping.UserMapper;
import com.crackelets.bigfun.platform.security.resource.UserResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Users", description = "Create, read, update and delete users")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/userss")
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    public UserController(UserService userService, UserMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }


    @PostMapping("/auth/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticateRequest request) {
        //if(request!=null) throw new RuntimeException("El request esta vacio");
        return userService.authentication(request);
        //return ResponseEntity.ok(request);
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        Page<UserResource> resources = mapper.modelListToPage(userService.getAll(), pageable);
        return ResponseEntity.ok(resources);
    }

}
