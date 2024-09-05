package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.ApiResponse;
import com.epam.user.management.application.dto.UserProfileRequest;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.service.JwtService;
import com.epam.user.management.application.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/profile")
@Log
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class ProfileController {

    private final JwtService jwtService;
    private final ProfileService profileService;


    @GetMapping("/view")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);

        UserResponse userResponse = profileService.getProfileByUsers(email);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User profile retrieved successfully")
                .data(userResponse)
                .build();
        return ResponseEntity.ok(apiResponse);

    }


//    @PutMapping(value = "/editProfile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse<Void>> editUserProfile(HttpServletRequest request, @RequestParam("email") String email,
//                                                           @RequestParam("firstName") String firstName,
//                                                           @RequestParam("lastName") String lastName,
//                                                           @RequestParam("password") String password,
//                                                           @RequestParam("gender") String gender,
//                                                           @RequestParam("country") String country,
//                                                           @RequestParam("city") String city,
//                                                           @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
//        profileService.updateUser(email, firstName, lastName, gender, password, country, city, file);
//        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
//                .status(HttpStatus.OK.value())
//                .message("Profile updated successfully")
//                .build();
//        return ResponseEntity.ok(apiResponse);
//    }
@PutMapping(value = "/editProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ApiResponse<Void>> editUserProfile(HttpServletRequest request,
                                                         @Valid @ModelAttribute UserProfileRequest userProfileRequest) throws IOException {
    profileService.updateUser(
            userProfileRequest.getEmail(),
            userProfileRequest.getFirstName(),
            userProfileRequest.getLastName(),
            userProfileRequest.getGender(),
            userProfileRequest.getPassword(),
            userProfileRequest.getCountry(),
            userProfileRequest.getCity(),
            userProfileRequest.getFile()
    );

    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message("Profile updated successfully")
            .build();

    return ResponseEntity.ok(apiResponse);
}
}
