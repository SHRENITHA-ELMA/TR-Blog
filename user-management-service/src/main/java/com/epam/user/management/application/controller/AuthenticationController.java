package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.LoginRequest;
import com.epam.user.management.application.dto.LoginResponse;
import com.epam.user.management.application.dto.RegisterRequest;
import com.epam.user.management.application.dto.RegisterResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.service.AuthenticationServiceImpl;
import com.epam.user.management.application.service.JwtService;
import com.epam.user.management.application.service.JwtServiceImpl;
import org.springframework.http.MediaType;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.AuthenticationServiceImpl;
import com.epam.user.management.application.service.JwtServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtServiceImpl jwtService;

    private final AuthenticationServiceImpl authenticationService;

    public AuthenticationController(JwtServiceImpl jwtService, AuthenticationServiceImpl authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {

        RegisterResponse registerResponse = authenticationService.register(registerRequest);

        return ResponseEntity.ok(registerResponse);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        User authenticatedUser;
        try {
            authenticatedUser = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (Exception e) {
            LoginResponse loginResponse = LoginResponse.builder().message(e.getMessage()).build();
            return ResponseEntity.ok(loginResponse);
        }

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken).expiresIn(jwtService.getExpirationTime()).message("Login Successful").role(authenticatedUser.getRole())
                .build();

        return ResponseEntity.ok(loginResponse);
    }
}