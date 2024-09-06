package com.epam.user.management.application.utility;

import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenUtils {
    public final JwtService jwtService;
    public final UserRepository userRepository;

    public Optional<User> getUserFromRequest(HttpServletRequest request){
        String token=request.getHeader("Authorization").substring(7);
        String email=jwtService.extractUsername(token);
        return userRepository.findByEmail(email);
    }

    public String getEmailFromRequest(HttpServletRequest request){
        String token=request.getHeader("Authorization").substring(7);
        return jwtService.extractUsername(token);
    }
}