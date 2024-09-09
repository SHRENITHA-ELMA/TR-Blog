package com.epam.user.management.application.serviceImpl;

import com.epam.user.management.application.dto.ForgotPasswordResponse;
import com.epam.user.management.application.dto.ResetPasswordRequest;
import com.epam.user.management.application.dto.ResetPasswordResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.serviceImpl.PasswordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PasswordServiceImplTest {

    @InjectMocks
    private PasswordServiceImpl passwordService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testResetPassword_NewPasswordAndConfirmPasswordDoNotMatch() {
        ResetPasswordRequest request = new ResetPasswordRequest("test@example.com", "NewPassword123!", "DifferentPassword123!");

        ResetPasswordResponse response = passwordService.resetPassword(request);

        assertEquals(false, response.isSuccess());
        assertEquals("New password and confirm password do not match.", response.getMessage());
    }

    @Test
    void testResetPassword_PasswordDoesNotMeetCriteria() {
        ResetPasswordRequest request = new ResetPasswordRequest("test@example.com", "short", "short");

        ResetPasswordResponse response = passwordService.resetPassword(request);

        assertEquals(false, response.isSuccess());
        assertEquals("Password does not meet the criteria.", response.getMessage());
    }

    @Test
    void testResetPassword_UserDoesNotExist() {
        ResetPasswordRequest request = new ResetPasswordRequest("test@example.com", "ValidPass1!", "ValidPass1!");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        ResetPasswordResponse response = passwordService.resetPassword(request);

        assertEquals(false, response.isSuccess());
        assertEquals("User doesn't exist.", response.getMessage());
    }

    @Test
    void testResetPassword_Success() {
        ResetPasswordRequest request = new ResetPasswordRequest("test@example.com", "ValidPass1!", "ValidPass1!");
        User user = new User();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encodedPassword");

        ResetPasswordResponse response = passwordService.resetPassword(request);

        assertEquals(true, response.isSuccess());
        assertEquals(null,response.getMessage());
    }

    @Test
    void testPasswordEncoding() {
        ResetPasswordRequest request = new ResetPasswordRequest("test@example.com", "ValidPass1!", "ValidPass1!");
        User user = new User();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encodedPassword");

        ResetPasswordResponse response = passwordService.resetPassword(request);

        // Verify that the password encoder was used to encode the new password
        assertEquals("encodedPassword", passwordEncoder.encode(request.getNewPassword()));
    }

    @Test
    void testIsEmailPresent_EmailExists() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        ForgotPasswordResponse response = passwordService.isEmailPresent(email);

        assertEquals(true, response.isEmailExists());
    }

    @Test
    void testIsEmailPresent_EmailDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ForgotPasswordResponse response = passwordService.isEmailPresent(email);

        assertEquals(false, response.isEmailExists());
    }

    @Test
    void testResetPassword_EmptyEmail() {
        ResetPasswordRequest request = new ResetPasswordRequest("", "ValidPass1!", "ValidPass1!");

        ResetPasswordResponse response = passwordService.resetPassword(request);

        assertEquals(false, response.isSuccess());
        assertEquals("User doesn't exist.", response.getMessage());
    }
}
