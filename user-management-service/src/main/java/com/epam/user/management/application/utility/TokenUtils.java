package com.epam.user.management.application.utility;

import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class TokenUtils {
    JwtService jwtService;
    UserRepository userRepository;

    public TokenUtils(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public Optional<User> getUserFromRequest(HttpServletRequest request){
        String token=request.getHeader("Authorization").substring(7);
        String email=jwtService.extractUsername(token);
        return userRepository.findByEmail(email);
    }
}
