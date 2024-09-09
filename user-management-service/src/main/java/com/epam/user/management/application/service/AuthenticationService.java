package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.ApiResponse;
import com.epam.user.management.application.dto.LoginResponse;
import com.epam.user.management.application.dto.LogoutResponse;
import com.epam.user.management.application.dto.RegisterRequest;
import com.epam.user.management.application.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    ApiResponse<LoginResponse> authenticate(String email, String password);

    String register(RegisterRequest registerRequest);

    public LogoutResponse logout(HttpServletRequest request);
}
