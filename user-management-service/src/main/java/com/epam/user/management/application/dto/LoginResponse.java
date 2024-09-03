package com.epam.user.management.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String message;
    private String token;
    private String role;
    private long expiresIn;
}
