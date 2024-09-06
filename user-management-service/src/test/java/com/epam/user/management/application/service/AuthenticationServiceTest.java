package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.RegisterRequest;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UserAlreadyExistsException;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.serviceImpl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("johndoe@example.com");
        request.setPassword("password");
        request.setGender("Male");
        request.setCountry("USA");
        request.setCity("New York");

        // Mock repository findByEmail to return empty
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Mock password encoder
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_password");

        // Call the service method
        String response = authenticationService.register(request);

        // Verify repository save was called
        verify(userRepository, times(1)).save(any(User.class));

        // Assert the success message
        assertEquals("User registration successful", response);
    }

//    @Test(expected = UserAlreadyExistsException.class)
    public void testRegister_UserAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("johndoe@example.com");

        // Mock repository findByEmail to return a user
        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(new User()));

        // This should throw an exception
        authenticationService.register(request);
    }

    @Test
    void authenticate_validCredentials_returnsUser() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = authenticationService.authenticate(email, password);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void authenticate_invalidCredentials_throwsException() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongPassword";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> authenticationService.authenticate(email, password));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void authenticate_userNotFound_throwsException() {
        // Arrange
        String email = "test@example.com";
        String password = "password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(email, password));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(email);
    }


    @Test
    void testIsTokenBlacklisted_False() {
        String token = "sampleToken";

        // Verify that a token not in the blacklist returns false
        assertFalse(jwtService.isTokenBlacklisted(token));
    }
}

