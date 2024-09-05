package com.epam.user.management.application;

import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.FileStorageService;
import com.epam.user.management.application.service.ProfileServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileServiceTest {

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
   private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
@Test
void testGetProfileByUsers_UserFound() {
    String email = "test@example.com";
    User user = new User();
    user.setEmail(email);
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setImageUrl("http://localhost:8080/uploads/test.png");
    user.setRole("User");

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(new UserResponse());

    UserResponse response = profileService.getProfileByUsers(email);

    assertNotNull(response);
    verify(userRepository).findByEmail(email);
}

    @Test
    void testGetProfileByUsers_UserNotFound() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            profileService.getProfileByUsers(email);
        });

        assertEquals("User not found with email: " + email, thrown.getMessage());
        verify(userRepository).findByEmail(email);
    }


    @Test
    void testUpdateUser_Success() throws IOException {
        String email = "test@example.com";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test".getBytes()));
        when(fileStorageService.storeFile(file)).thenReturn("file-path");

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setRole("User");
        user.setEnabled(true);
        user.setCreatedAt(new Date());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("Admin@1234")).thenReturn("encoded-password");

        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName("John");
        userResponse.setLastName("Doe");
        userResponse.setEmail(email);
        userResponse.setGender("Male");
        userResponse.setCity("City");
        userResponse.setCountry("Country");
        userResponse.setImageUrl("file-path");

        profileService.updateUser(email, "John", "Doe", "Male", "Admin@1234", "Country", "City", file);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() throws IOException {
        // Arrange
        String email = "test@example.com";
        MultipartFile file = mock(MultipartFile.class);
        when(fileStorageService.storeFile(file)).thenReturn("uploads/test.png");

        // Simulate user not found
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            profileService.updateUser(email, "John", "Doe", "Male", "Admin@1234", "Country", "City", file);
        });

        // Assert
        assertEquals("User not found with email: " + email, thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_PasswordInvalid() {
        // Arrange
        String email = "user@example.com";
        String password = "invalid";
        MultipartFile file = mock(MultipartFile.class);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                profileService.updateUser(email, "John", "Doe", "Male", password, "Country", "City", file)
        );
        assertEquals("Password does not meet the required criteria.", exception.getMessage());
    }

}
