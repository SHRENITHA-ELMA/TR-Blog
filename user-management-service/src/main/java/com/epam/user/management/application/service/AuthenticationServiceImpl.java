package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.LogoutResponse;
import com.epam.user.management.application.dto.RegisterRequest;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UserAlreadyExistsException;
import com.epam.user.management.application.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl (
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService

    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    @Override
    public String register(RegisterRequest registerRequest) {
        Optional<User> user = userRepository.findByEmail(registerRequest.getEmail());

        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
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
            return "User registration successful";
        }
    }

    @Override
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
    @Override
    public LogoutResponse logout(String token) {
        jwtService.blacklistToken(token);
        return new LogoutResponse("Logout successful.");
    }
}
