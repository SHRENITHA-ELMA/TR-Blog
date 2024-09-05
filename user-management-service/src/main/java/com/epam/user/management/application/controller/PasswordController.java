package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.ApiResponse;
import com.epam.user.management.application.dto.ForgotPasswordRequest;
import com.epam.user.management.application.dto.ForgotPasswordResponse;
import com.epam.user.management.application.dto.ResetPasswordRequest;
import com.epam.user.management.application.dto.ResetPasswordResponse;
import com.epam.user.management.application.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordController {
    public final PasswordService passwordService;


    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<ForgotPasswordResponse>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        ForgotPasswordResponse response = passwordService.isEmailPresent(forgotPasswordRequest.getEmail());
        String message = response.isEmailExists() ? "Email is present, please change your password." : "User doesn't exist.";
        HttpStatus status = response.isEmailExists() ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<ForgotPasswordResponse> apiResponse = ApiResponse.<ForgotPasswordResponse>builder()
                .status(status.value())
                .message(message)
                .build();

        return ResponseEntity.status(status).body(apiResponse);
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<ResetPasswordResponse>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordResponse response = passwordService.resetPassword(resetPasswordRequest);
        String message;
        HttpStatus status;

        if (!response.isSuccess()) {
            message = response.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else {
            message = "Password has been reset successfully.";
            status = HttpStatus.OK;
        }

        ApiResponse<ResetPasswordResponse> apiResponse = ApiResponse.<ResetPasswordResponse>builder()
                .status(status.value())
                .message(message)
                .build();

        return ResponseEntity.status(status).body(apiResponse);
    }
}
