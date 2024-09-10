package com.epam.user.management.application.controller;


import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.dto.VerificationResponse;
import com.epam.user.management.application.service.JwtService;

import com.epam.user.management.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RequestMapping("users")
@RestController
public class UserController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    UserService userservice;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAdmin(@RequestParam String token) {
        String jwtToken = token;
        String email = jwtService.extractUsername(jwtToken);
        boolean isAdmin = userservice.isAdmin(email);
        boolean tokenExpired= jwtService.isTokenExpired(token);

        if (isAdmin&&(!tokenExpired)) {
            VerificationResponse response=VerificationResponse.builder().status("202").message("User verified").build();
            return ResponseEntity.ok(response);
        } else {
            VerificationResponse response=VerificationResponse.builder().status("403").message("User Not Authorized").error("Forbidden").build();
            return ResponseEntity.status(403).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserFromToken(@RequestParam String token){

        String email = jwtService.extractUsername(token);

        boolean tokenExpired= jwtService.isTokenExpired(token);
        try{
            if(!tokenExpired){
                UserResponse userResponse = userservice.getUser(email);
                return ResponseEntity.ok(userResponse);
            }else{
                return ResponseEntity.status(401).build();
            }
        }catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }
}

