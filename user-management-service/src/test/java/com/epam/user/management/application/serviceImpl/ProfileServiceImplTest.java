package com.epam.user.management.application.serviceImpl;

import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.EmailMismatchException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@RequiredArgsConstructor
public class ProfileServiceImplTest {

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
    void testUpdateUserSuccessfully() throws IOException {
        String email = "test@example.com";
        String emailFromToken = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String gender = "Male";
        String password = "Password123!";
        String country = "USA";
        String city = "New York";
        MultipartFile file = mock(MultipartFile.class);
        User existingUser = User.builder()
            .id(1L)
            .firstName("John")  // Add other required fields if necessary
            .lastName("Doe")    // Add other required fields if necessary
            .email(email)
            .password("oldPassword")
            .imageUrl("oldImageUrl")
            .createdAt(new Date())
            .isEnabled(true)
            .role("ROLE_USER")
            .gender("Male")  // Add other required fields if necessary
            .country("USA")  // Add other required fields if necessary
            .city("New York") // Add other required fields if necessary
            .build();


        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(fileStorageService.storeFile(file)).thenReturn("newImageUrl");
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        profileService.updateUser(emailFromToken, email, firstName, lastName, gender, password, country, city, file);

        verify(userRepository).save(argThat(user -> user.getFirstName().equals(firstName)
            && user.getLastName().equals(lastName)
            && user.getPassword().equals("encodedPassword")
            && user.getImageUrl().equals("newImageUrl")));
    }

    @Test
    void testPasswordInvalid() {
        String invalidPassword = "123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            profileService.updateUser("test@example.com", "test@example.com", "John", "Doe", "Male", invalidPassword, "USA", "New York", null);
        });

        assertEquals("Password does not meet the required criteria.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testEmailMismatch() {
        String emailFromToken = "token@example.com";
        String email = "test@example.com";

        EmailMismatchException exception = assertThrows(EmailMismatchException.class, () -> {
            profileService.updateUser(emailFromToken, email, "John", "Doe", "Male", "Password123!", "USA", "New York", null);
        });

        assertEquals("Email doesn't match", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUserNotFound() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            profileService.updateUser(email, email, "John", "Doe", "Male", "Password123!", "USA", "New York", null);
        });

        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateWithOnlyMandatoryFields() throws IOException {
        String email = "test@example.com";
        User existingUser = User.builder()
                .id(1L)
                .firstName("John")  // Add other required fields if necessary
                .lastName("Doe")    // Add other required fields if necessary
                .email(email)
                .password("oldPassword")
                .imageUrl("oldImageUrl")
                .createdAt(new Date())
                .isEnabled(true)
                .role("ROLE_USER")
                .gender("Male")  // Add other required fields if necessary
                .country("USA")  // Add other required fields if necessary
                .city("New York") // Add other required fields if necessary
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        profileService.updateUser(email, email, "John", "Doe", "Male", null, "USA", "New York", null);

        verify(userRepository).save(argThat(user -> user.getFirstName().equals("John")
                && user.getLastName().equals("Doe")
                && user.getCity().equals("New York")
                && user.getCountry().equals("USA")));
    }

}
