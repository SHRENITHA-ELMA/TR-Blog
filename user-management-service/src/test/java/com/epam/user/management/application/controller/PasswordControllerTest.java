package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.ApiResponse;
import com.epam.user.management.application.dto.ForgotPasswordRequest;
import com.epam.user.management.application.dto.ForgotPasswordResponse;
import com.epam.user.management.application.dto.ResetPasswordRequest;
import com.epam.user.management.application.dto.ResetPasswordResponse;
import com.epam.user.management.application.service.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class PasswordControllerTest {

    @InjectMocks
    private PasswordController passwordController;

    @Mock
    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testForgotPassword_EmailExists() {
        // Arrange
        String email = "test@example.com";
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        ForgotPasswordResponse response = new ForgotPasswordResponse(true);
        when(passwordService.isEmailPresent(email)).thenReturn(response);

        // Act
        ResponseEntity<ApiResponse<ForgotPasswordResponse>> result = passwordController.forgotPassword(request);

        // Assert
        ApiResponse<ForgotPasswordResponse> body = result.getBody();
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertEquals("Email is present, please change your password.", body.getMessage());
        assertNull(body.getData()); // Data should be null
    }


    @Test
    void testForgotPassword_EmailDoesNotExist() {
        // Arrange
        String email = "test@example.com";
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        ForgotPasswordResponse response = new ForgotPasswordResponse(false);
        when(passwordService.isEmailPresent(email)).thenReturn(response);

        // Act
        ResponseEntity<ApiResponse<ForgotPasswordResponse>> result = passwordController.forgotPassword(request);

        // Assert
        ApiResponse<ForgotPasswordResponse> body = result.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCodeValue());
        assertEquals("User doesn't exist.", body.getMessage());
        assertNull(body.getData()); // Data should be null
    }


    @Test
    void testResetPassword_Success() {
        // Arrange
        ResetPasswordRequest request = new ResetPasswordRequest("user@example.com", "newPassword", "newPassword");
        ResetPasswordResponse response = new ResetPasswordResponse(true, null);
        when(passwordService.resetPassword(request)).thenReturn(response);

        // Act
        ResponseEntity<ApiResponse<ResetPasswordResponse>> result = passwordController.resetPassword(request);

        // Assert
        ApiResponse<ResetPasswordResponse> body = result.getBody();
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertEquals("Password has been reset successfully.", body.getMessage());
        assertNull(body.getData()); // Data should be null
    }

    @Test
    void testResetPassword_Failure() {
        // Arrange
        ResetPasswordRequest request = new ResetPasswordRequest("user@example.com", "newPassword", "newPassword");
        ResetPasswordResponse response = new ResetPasswordResponse(false, "Password reset failed");
        when(passwordService.resetPassword(request)).thenReturn(response);

        // Act
        ResponseEntity<ApiResponse<ResetPasswordResponse>> result = passwordController.resetPassword(request);

        // Assert
        ApiResponse<ResetPasswordResponse> body = result.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCodeValue());
        assertEquals("Password reset failed", body.getMessage());
        assertNull(body.getData()); // Data should be null
    }

}


