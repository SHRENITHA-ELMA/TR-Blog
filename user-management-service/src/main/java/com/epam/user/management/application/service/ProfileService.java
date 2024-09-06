package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    UserResponse getProfileByUsers(String email);
    void updateUser(String emailFromToken,String email , String firstName , String lastName , String gender,String password,String country , String city , MultipartFile file) throws IOException;
}
