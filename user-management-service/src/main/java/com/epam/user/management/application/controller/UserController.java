package com.epam.user.management.application.controller;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.dto.VerificationResponse;
import com.epam.user.management.application.service.JwtService;
import com.epam.user.management.application.service.UserServiceOwn;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("users")
@RestController
@AllArgsConstructor
public class UserController {
    private JwtService jwtService;
    private UserServiceOwn userservice;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAdmin(@RequestParam String token) {
        String jwtToken = token;
        String email = jwtService.extractUsername(jwtToken);
        boolean isAdmin = userservice.isAdmin(email);
        boolean tokenExpired= jwtService.isTokenExpired(token);

        if (isAdmin&&(!tokenExpired)) {
            VerificationResponse response= VerificationResponse.builder().status("202").message("User verified").build();
            return ResponseEntity.ok(response);
        } else {
            VerificationResponse response= VerificationResponse.builder().status("403").message("User Not Authorized").build();
            return ResponseEntity.status(403).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserFromToken(@RequestParam String token) {

        String email = jwtService.extractUsername(token);
        boolean tokenExpired= jwtService.isTokenExpired(token);

        if (!tokenExpired) {
            UserResponse userResponse = userservice.getUser(email);
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}

