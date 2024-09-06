package com.epam.user.management.application.serviceImpl;

import com.epam.user.management.application.dto.LogoutResponse;
import com.epam.user.management.application.dto.RegisterRequest;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UserAlreadyExistsException;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;



    @Override
    public String register(RegisterRequest registerRequest) {
        if (userExists(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User newUser = buildUser(registerRequest);
        userRepository.save(newUser);

        return "User registration successful";
    }

    private boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private User buildUser(RegisterRequest registerRequest) {
        return User.builder()
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
    public LogoutResponse logout(HttpServletRequest request) {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return new LogoutResponse("User is unauthorized", HttpStatus.UNAUTHORIZED.value());
            }
            String token = authorization.substring(7);
            jwtService.blacklistToken(token);
            return new LogoutResponse("Logout successful", HttpStatus.OK.value());
        } catch (Exception e) {
            return new LogoutResponse("Logout failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
