package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.RegisterRequest;
import com.epam.user.management.application.dto.RegisterResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class AuthenticationServiceImpl {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            ObjectMapper objectMapper

    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        Optional<User> user = userRepository.findByEmail(registerRequest.getEmail());

        if (user.isPresent()) {
            return RegisterResponse.builder().message("User with email already exists").build();
        } else {
            User newUser = User.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .gender(registerRequest.getGender())
                    .country(registerRequest.getCountry())
                    .city(registerRequest.getCity())
                    .role("User")
                    .isEnabled(true)
                    .build();
            userRepository.save(newUser);
            return RegisterResponse.builder().message("User registration successful").build();
        }
    }


    public User authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        return userRepository.findByEmail(email)
                .orElseThrow();
    }
}
