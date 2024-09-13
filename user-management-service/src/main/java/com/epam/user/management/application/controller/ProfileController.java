package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.ApiResponse;
import com.epam.user.management.application.dto.UserProfileRequest;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.service.JwtService;
import com.epam.user.management.application.service.ProfileService;
import com.epam.user.management.application.utility.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/profile")
@Log
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    private final JwtService jwtService;
    private final ProfileService profileService;
    private final TokenUtils tokenUtils;

    @GetMapping("/view")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(HttpServletRequest request) {
        String email=tokenUtils.getEmailFromRequest(request);
        UserResponse userResponse = profileService.getProfileByUsers(email);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder().status(HttpStatus.OK.value()).message("User profile retrieved successfully").data(userResponse).build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping(value = "/editProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> editUserProfile(HttpServletRequest request, @Valid @ModelAttribute UserProfileRequest userProfileRequest) throws IOException {
        String emailFromToken=tokenUtils.getEmailFromRequest(request);
        profileService.updateUser(emailFromToken, userProfileRequest.getEmail(), userProfileRequest.getFirstName(), userProfileRequest.getLastName(), userProfileRequest.getGender(), userProfileRequest.getPassword(), userProfileRequest.getCountry(), userProfileRequest.getCity(), userProfileRequest.getFile());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().status(HttpStatus.OK.value()).message("Profile updated successfully").build();
        return ResponseEntity.ok(apiResponse);
    }
}
