package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        String message = authenticationService.register(registerRequest);
        ApiResponse<Object> apiResponse = ApiResponse.builder().status(HttpStatus.OK.value()).message(message).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            ApiResponse<LoginResponse> response = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<LoginResponse> errorResponse = ApiResponse.<LoginResponse>builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(HttpServletRequest request) {
        LogoutResponse logoutResponse = authenticationService.logout(request);
        ApiResponse<LogoutResponse> apiResponse = ApiResponse.<LogoutResponse>builder()
                .status(logoutResponse.getStatus())
                .message(logoutResponse.getMessage())
                .build();
        return ResponseEntity.status(logoutResponse.getStatus()).body(apiResponse);
    }
}