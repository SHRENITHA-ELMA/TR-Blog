package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.ApiResponse;
import com.epam.user.management.application.dto.UserProfileRequest;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.service.FileStorageService;
import com.epam.user.management.application.service.JwtService;
import com.epam.user.management.application.service.ProfileService;
import com.epam.user.management.application.utility.TokenUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

class ProfileControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private ProfileService profileService;

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProfileController profileController;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer test-token");
    }

    @Test
    void getProfile_ShouldReturnUserProfileSuccessfully() {
        // Arrange
        String email = "user@example.com";
        MockHttpServletRequest request = new MockHttpServletRequest();
        UserResponse userResponse = new UserResponse(); // Set the fields of the response if necessary
        when(tokenUtils.getEmailFromRequest(request)).thenReturn(email);
        when(profileService.getProfileByUsers(email)).thenReturn(userResponse);

        // Act
        ResponseEntity<ApiResponse<UserResponse>> response = profileController.getProfile(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User profile retrieved successfully", response.getBody().getMessage());
        assertEquals(userResponse, response.getBody().getData());
        verify(tokenUtils).getEmailFromRequest(request);
        verify(profileService).getProfileByUsers(email);
    }

    @Test
    void editUserProfile_ShouldUpdateProfileSuccessfully() throws IOException {
        // Arrange
        String emailFromToken = "user@example.com";
        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setEmail("newuser@example.com"); // Set other fields as necessary
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(tokenUtils.getEmailFromRequest(request)).thenReturn(emailFromToken);

        // Act
        ResponseEntity<ApiResponse<Void>> response = profileController.editUserProfile(request, userProfileRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Profile updated successfully", response.getBody().getMessage());
        verify(tokenUtils).getEmailFromRequest(request);
        verify(profileService).updateUser(
                eq(emailFromToken),
                eq(userProfileRequest.getEmail()),
                eq(userProfileRequest.getFirstName()),
                eq(userProfileRequest.getLastName()),
                eq(userProfileRequest.getGender()),
                eq(userProfileRequest.getPassword()),
                eq(userProfileRequest.getCountry()),
                eq(userProfileRequest.getCity()),
                eq(userProfileRequest.getFile())
        );
    }

    @Test
    void editUserProfile_ShouldHandleIOException() throws IOException {
        // Arrange
        String emailFromToken = "test@example.com";
        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setEmail("test@example.com");
        userProfileRequest.setFirstName("John");
        userProfileRequest.setLastName("Doe");
        userProfileRequest.setGender("Male");
        userProfileRequest.setPassword("newPassword123");
        userProfileRequest.setCountry("USA");
        userProfileRequest.setCity("New York");
        MockMultipartFile file = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "test-image".getBytes());
        userProfileRequest.setFile(file);

        when(tokenUtils.getEmailFromRequest(request)).thenReturn(emailFromToken);
        doThrow(IOException.class).when(profileService).updateUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any());

        // Act & Assert
        assertThrows(IOException.class, () -> profileController.editUserProfile(request, userProfileRequest));

        verify(profileService, times(1)).updateUser(
                eq(emailFromToken),
                eq(userProfileRequest.getEmail()),
                eq(userProfileRequest.getFirstName()),
                eq(userProfileRequest.getLastName()),
                eq(userProfileRequest.getGender()),
                eq(userProfileRequest.getPassword()),
                eq(userProfileRequest.getCountry()),
                eq(userProfileRequest.getCity()),
                eq(file)
        );
    }
}

