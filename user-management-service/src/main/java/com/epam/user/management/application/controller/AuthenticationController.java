package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;


    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        String message=authenticationService.register(registerRequest);
        ApiResponse<Object>apiResponse=ApiResponse.builder().status(HttpStatus.OK.value()).message(message).build();
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        User authenticatedUser;
        try {
            authenticatedUser = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (Exception e) {
            LoginResponse loginResponse = LoginResponse.builder().build();
            return new ResponseEntity<>(ApiResponse.<LoginResponse>builder().status(HttpStatus.UNAUTHORIZED.value()).message(e.getMessage()).build(),HttpStatus.UNAUTHORIZED);
        }

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken).expiresIn(jwtService.getExpirationTime()).role(authenticatedUser.getRole())
                .build();

        return new ResponseEntity<>(ApiResponse.<LoginResponse>builder().status(HttpStatus.OK.value()).message("Login Successful").data(loginResponse).build(),HttpStatus.OK);
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(HttpServletRequest request) {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                ApiResponse<LogoutResponse> apiResponse = ApiResponse.<LogoutResponse>builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("User is unauthorized")
                        .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }
            String token = authorization.substring(7);
            String email = jwtService.extractUsername(token);
            LogoutResponse logoutResponse = authenticationService.logout(email);

            ApiResponse<LogoutResponse> apiResponse = ApiResponse.<LogoutResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Logout successful")
                    .build();

            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            ApiResponse<LogoutResponse> apiResponse = ApiResponse.<LogoutResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Logout failed: " + e.getMessage())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}