package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.UserResponse;

public interface UserService {
    boolean isAdmin(String email);
    UserResponse getUser(String email);

}
