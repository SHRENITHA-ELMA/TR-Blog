package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.ForgotPasswordResponse;
import com.epam.user.management.application.dto.ResetPasswordRequest;
import com.epam.user.management.application.dto.ResetPasswordResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,12}$";

    private boolean isPasswordValid(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

    @Transactional
    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String userEmail = resetPasswordRequest.getEmail();
        String newPassword = resetPasswordRequest.getNewPassword();
        String confirmPassword = resetPasswordRequest.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            return new ResetPasswordResponse(false, "New password and confirm password do not match.");
        }
        if (!isPasswordValid(newPassword)) {
            return new ResetPasswordResponse(false, "Password does not meet the criteria.");
        }

        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return new ResetPasswordResponse(false, "User doesn't exist.");
        }

        User userEntity = userOptional.get();
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
        return new ResetPasswordResponse(true, null);
    }

    @Override
    public ForgotPasswordResponse isEmailPresent(String email) {
        boolean emailExists = userRepository.findByEmail(email).isPresent();
        return new ForgotPasswordResponse(emailExists);
    }
}
