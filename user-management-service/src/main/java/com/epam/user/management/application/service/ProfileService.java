package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.UserProfileRequest;
import com.epam.user.management.application.dto.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    UserResponse getProfileByUsers(String email);
    void updateUser(String emailFromToken, UserProfileRequest userProfileRequest) throws IOException;
}
