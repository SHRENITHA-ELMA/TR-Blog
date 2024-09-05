package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.ForgotPasswordResponse;
import com.epam.user.management.application.dto.ResetPasswordRequest;
import com.epam.user.management.application.dto.ResetPasswordResponse;
import jakarta.transaction.Transactional;

public interface PasswordService {
    @Transactional
    ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest);
    ForgotPasswordResponse isEmailPresent(String email);
}
