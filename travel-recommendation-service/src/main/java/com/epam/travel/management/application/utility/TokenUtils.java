package com.epam.travel.management.application.utility;

import com.epam.travel.management.application.entity.User;
import com.epam.travel.management.application.feignClient.UserClient;
import com.epam.travel.management.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class TokenUtils {

    private final JwtService jwtService;
    private final UserClient userClient;


    public Optional<User> getUserFromRequest(HttpServletRequest request){
        String token=request.getHeader("Authorization").substring(7);
        String email=jwtService.extractUsername(token);
        return userClient.getUserByEmail(email);
    }
}
