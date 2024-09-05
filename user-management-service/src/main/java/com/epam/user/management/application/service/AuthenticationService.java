package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.LogoutResponse;
import com.epam.user.management.application.dto.RegisterRequest;
import com.epam.user.management.application.entity.User;

public interface AuthenticationService {
    public User authenticate(String email, String password);

    public String register(RegisterRequest registerRequest);

    public LogoutResponse logout(String token);
}
