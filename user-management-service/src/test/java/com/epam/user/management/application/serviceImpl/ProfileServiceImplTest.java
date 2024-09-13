package com.epam.user.management.application.serviceImpl;

import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.EmailMismatchException;
import com.epam.user.management.application.exception.InvalidFileFormatException;
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

//    @Test
//    void testUpdateUserSuccessfully() throws IOException {
//        String email = "test@example.com";
//        String emailFromToken = "test@example.com";
//        String firstName = "John";
//        String lastName = "Doe";
//        String gender = "Male";
//        String password = "Password123!";
//        String country = "USA";
//        String city = "New York";
//        MultipartFile file = mock(MultipartFile.class);
//        User existingUser = User.builder()
//            .id(1L)
//            .firstName("John")  // Add other required fields if necessary
//            .lastName("Doe")    // Add other required fields if necessary
//            .email(email)
//            .password("oldPassword")
//            .imageUrl("oldImageUrl.jpg")
//            .createdAt(new Date())
//            .isEnabled(true)
//            .role("ROLE_USER")
//            .gender("Male")  // Add other required fields if necessary
//            .country("USA")  // Add other required fields if necessary
//            .city("New York") // Add other required fields if necessary
//            .build();
//
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
//        when(fileStorageService.storeFile(file)).thenReturn("newImageUrl.jpg");
//        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
//
//        profileService.updateUser(emailFromToken, email, firstName, lastName, gender, password, country, city, file);
//
//        verify(userRepository).save(argThat(user -> user.getFirstName().equals(firstName)
//            && user.getLastName().equals(lastName)
//            && user.getPassword().equals("encodedPassword")
//            && user.getImageUrl().equals("newImageUrl.jpg")));
//    }
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

    // Create a mock MultipartFile with a valid content type
    MultipartFile file = mock(MultipartFile.class);
    when(file.isEmpty()).thenReturn(false);
    when(file.getContentType()).thenReturn("image/jpeg"); // Set a valid content type

    User existingUser = User.builder()
            .id(1L)
            .firstName("John")  // Add other required fields if necessary
            .lastName("Doe")    // Add other required fields if necessary
            .email(email)
            .password("oldPassword")
            .imageUrl("oldImageUrl.jpg")
            .createdAt(new Date())
            .isEnabled(true)
            .role("ROLE_USER")
            .gender("Male")  // Add other required fields if necessary
            .country("USA")  // Add other required fields if necessary
            .city("New York") // Add other required fields if necessary
            .build();

    // Mock the repository and file storage service
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
    when(fileStorageService.storeFile(file)).thenReturn("newImageUrl.jpg");
    when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

    // Act
    profileService.updateUser(emailFromToken, email, firstName, lastName, gender, password, country, city, file);

    // Assert
    verify(userRepository).save(argThat(user ->
            user.getFirstName().equals(firstName) &&
                    user.getLastName().equals(lastName) &&
                    user.getPassword().equals("encodedPassword") &&
                    user.getImageUrl().equals("newImageUrl.jpg")));
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
    @Test
    void testUpdateUser_EmailMismatch_ThrowsEmailMismatchException() {
        EmailMismatchException exception = assertThrows(EmailMismatchException.class, () -> {
            profileService.updateUser("tokenEmail@example.com", "differentEmail@example.com", "John", "Doe", "Male",
                    null, "USA", "New York", null);
        });
        assertEquals("Email doesn't match", exception.getMessage());
    }
//    @Test
//void testUpdateUser_NoPasswordAndNoFile_ShouldRetainExistingValues() throws IOException {
//    // Mock the findByEmail to return the original user
//    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
//
//    // Call the method under test
//    profileService.updateUser("test@example.com", "test@example.com", "Jane", "Doe", "Female",
//            null, "Canada", "Toronto", null);
//
//    // Capture the user object passed to save
//    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
//    verify(userRepository).save(userCaptor.capture());
//
//    User updatedUser = userCaptor.getValue();  // Get the saved User object
//
//    // Assertions to check the updated values
//    assertEquals("Jane", updatedUser.getFirstName());
//    assertEquals("Doe", updatedUser.getLastName());
//    assertEquals("Canada", updatedUser.getCountry());
//    assertEquals("Toronto", updatedUser.getCity());
//    assertEquals("encodedPassword", updatedUser.getPassword());  // Retain existing password
//    assertEquals("old-image-url.jpg", updatedUser.getImageUrl());  // Retain existing image URL
//}
    @Test
    void testUpdateUser_UserNotFound_ThrowsUserNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            profileService.updateUser("test@example.com", "test@example.com", "John", "Doe", "Male",
                    null, "USA", "New York", null);
        });
        assertEquals("User not found with email: test@example.com", exception.getMessage());
    }
    @Test
    void testUpdateUser_InvalidFileType_ThrowsInvalidFileFormatException() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getContentType()).thenReturn("application/pdf");

        InvalidFileFormatException exception = assertThrows(InvalidFileFormatException.class, () -> {
            profileService.updateUser("test@example.com", "test@example.com", "John", "Doe", "Male",
                    null, "USA", "New York", mockFile);
        });
        assertEquals("File must be in JPEG,JPG or PNG format", exception.getMessage());
    }

}
//package com.epam.user.management.application.serviceImpl;

//import com.epam.user.management.application.dto.UserResponse;
//import com.epam.user.management.application.entity.User;
//import com.epam.user.management.application.exception.EmailMismatchException;
//import com.epam.user.management.application.exception.InvalidFileFormatException;
//import com.epam.user.management.application.exception.UnauthorizedAccessException;
//import com.epam.user.management.application.exception.UserNotFoundException;
//import com.epam.user.management.application.repository.UserRepository;
//import com.epam.user.management.application.service.FileStorageService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.multipart.MultipartFile;

//import java.io.IOException;
//import java.util.Date;
//import java.util.Optional;
//import java.util.regex.Pattern;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//class ProfileServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private FileStorageService fileStorageService;
//
//    @InjectMocks
//    private ProfileServiceImpl profileService;
//
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        user = User.builder()
//                .id(1L)
//                .email("test@example.com")
//                .password("encodedPassword")
//                .role("User")
//                .isEnabled(true)
//                .firstName("John")
//                .lastName("Doe")
//                .city("New York")
//                .country("USA")
//                .gender("Male")
//                .imageUrl("old-image-url.jpg")
//                .createdAt(new Date())
//                .updatedAt(new Date())
//                .build();
//    }
//
//    // Test for getProfileByUsers method
//
//    @Test
//    void testGetProfileByUsers_UserFoundAndValidRole_ReturnsUserResponse() {
//        // Mock repository to return user
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
//
//        // Mock object mapper to return a UserResponse
//        UserResponse userResponse = new UserResponse();
//        when(objectMapper.convertValue(any(User.class), eq(UserResponse.class))).thenReturn(userResponse);
//
//        // Execute the method
//        UserResponse response = profileService.getProfileByUsers("test@example.com");
//
//        // Validate the result
//        assertNotNull(response);
//        assertEquals(userResponse, response);
//        verify(userRepository).findByEmail(anyString());
//        verify(objectMapper).convertValue(user, UserResponse.class);
//    }
//
//    @Test
//    void testGetProfileByUsers_UserNotFound_ThrowsUserNotFoundException() {
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
//            profileService.getProfileByUsers("test@example.com");
//        });
//
//        assertEquals("User not found with email: test@example.com", exception.getMessage());
//    }
//
//    @Test
//    void testGetProfileByUsers_UnauthorizedRole_ThrowsUnauthorizedAccessException() {
//        // Modify the user to have an unauthorized role
//        user.setRole("Admin");
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
//
//        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
//            profileService.getProfileByUsers("test@example.com");
//        });
//
//        assertEquals("Access denied for users with role: Admin", exception.getMessage());
//    }
//
//    // Tests for updateUser method
//    @Test
//    void testUpdateUser_SuccessfulUpdate_WithValidPassword() throws IOException {
//        // Arrange
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
//
//        MultipartFile mockFile = mock(MultipartFile.class);
//        when(mockFile.isEmpty()).thenReturn(false);
//        when(fileStorageService.storeFile(mockFile)).thenReturn("new-image-url.jpg");
//        when(mockFile.getContentType()).thenReturn("image/jpeg");
//
//        // Act
//        profileService.updateUser("test@example.com", "test@example.com", "Jane", "Doe", "Female",
//                "ValidPassword1@", "Canada", "Toronto", mockFile);
//
//        // Assert
//        verify(userRepository).save(any(User.class));
//
//        assertEquals("Jane", user.getFirstName());
//        assertEquals("Doe", user.getLastName());
//        assertEquals("Canada", user.getCountry());
//        assertEquals("Toronto", user.getCity());
//        assertEquals("encodedNewPassword", user.getPassword());
//        assertEquals("new-image-url.jpg", user.getImageUrl());
//    }
//
//
//
////    @Test
////    void testUpdateUser_SuccessfulUpdate_WithPasswordAndFile() throws IOException {
////        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
////        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
////
////        MultipartFile mockFile = mock(MultipartFile.class);
////        when(mockFile.isEmpty()).thenReturn(false);
////        when(fileStorageService.storeFile(mockFile)).thenReturn("new-image-url.jpg");
////        when(mockFile.getContentType()).thenReturn("image/jpeg");
////
////        profileService.updateUser("test@example.com", "test@example.com", "Jane", "Doe", "Female",
////                "NewPassword1@", "Canada", "Toronto", mockFile);
////
////        verify(userRepository).save(any(User.class));
////
////        assertEquals("Jane", user.getFirstName());
////        assertEquals("Doe", user.getLastName());
////        assertEquals("Canada", user.getCountry());
////        assertEquals("Toronto", user.getCity());
////        assertEquals("encodedNewPassword", user.getPassword());
////        assertEquals("new-image-url.jpg", user.getImageUrl());
////    }
//
//    @Test
//    void testUpdateUser_EmailMismatch_ThrowsEmailMismatchException() {
//        EmailMismatchException exception = assertThrows(EmailMismatchException.class, () -> {
//            profileService.updateUser("tokenEmail@example.com", "differentEmail@example.com", "John", "Doe", "Male",
//                    null, "USA", "New York", null);
//        });
//        assertEquals("Email doesn't match", exception.getMessage());
//    }
//
//    @Test
//    void testUpdateUser_UserNotFound_ThrowsUserNotFoundException() {
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
//            profileService.updateUser("test@example.com", "test@example.com", "John", "Doe", "Male",
//                    null, "USA", "New York", null);
//        });
//        assertEquals("User not found with email: test@example.com", exception.getMessage());
//    }
//
//    @Test
//    void testUpdateUser_InvalidPassword_ThrowsIllegalArgumentException() {
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            profileService.updateUser("test@example.com", "test@example.com", "John", "Doe", "Male",
//                    "invalid", "USA", "New York", null);
//        });
//        assertEquals("Password does not meet the required criteria.", exception.getMessage());
//    }
//
//    @Test
//    void testUpdateUser_InvalidFileType_ThrowsInvalidFileFormatException() throws IOException {
//        MultipartFile mockFile = mock(MultipartFile.class);
//        when(mockFile.isEmpty()).thenReturn(false);
//        when(mockFile.getContentType()).thenReturn("application/pdf");
//
//        InvalidFileFormatException exception = assertThrows(InvalidFileFormatException.class, () -> {
//            profileService.updateUser("test@example.com", "test@example.com", "John", "Doe", "Male",
//                    null, "USA", "New York", mockFile);
//        });
//        assertEquals("File must be in JPEG,JPG or PNG format", exception.getMessage());
//    }
//
////    @Test
////    void testUpdateUser_NoPasswordAndNoFile_ShouldRetainExistingValues() throws IOException {
////        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
////
////        profileService.updateUser("test@example.com", "test@example.com", "Jane", "Doe", "Female",
////                null, "Canada", "Toronto", null);
////
////        verify(userRepository).save(any(User.class));
////
////        assertEquals("Jane", user.getFirstName());
////        assertEquals("Doe", user.getLastName());
////        assertEquals("Canada", user.getCountry());
////        assertEquals("Toronto", user.getCity());
////        assertEquals("encodedPassword", user.getPassword());  // Retain existing password
////        assertEquals("old-image-url.jpg", user.getImageUrl());  // Retain existing image URL
////    }
//@Test
//void testUpdateUser_NoPasswordAndNoFile_ShouldRetainExistingValues() throws IOException {
//    // Mock the findByEmail to return the original user
//    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
//
//    // Call the method under test
//    profileService.updateUser("test@example.com", "test@example.com", "Jane", "Doe", "Female",
//            null, "Canada", "Toronto", null);
//
//    // Capture the user object passed to save
//    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
//    verify(userRepository).save(userCaptor.capture());
//
//    User updatedUser = userCaptor.getValue();  // Get the saved User object
//
//    // Assertions to check the updated values
//    assertEquals("Jane", updatedUser.getFirstName());
//    assertEquals("Doe", updatedUser.getLastName());
//    assertEquals("Canada", updatedUser.getCountry());
//    assertEquals("Toronto", updatedUser.getCity());
//    assertEquals("encodedPassword", updatedUser.getPassword());  // Retain existing password
//    assertEquals("old-image-url.jpg", updatedUser.getImageUrl());  // Retain existing image URL
//}
//
////    @Test
////    void testUpdateUser_OnlyUpdateSomeFields_ShouldUpdateCorrectly() throws IOException {
////        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
////
////        profileService.updateUser("test@example.com", "test@example.com", "John", null, "Male",
////                null, null, "New York", null);
////
////        verify(userRepository).save(any(User.class));
////
////        assertEquals("John", user.getFirstName());
////        assertNull(user.getLastName());  // No last name provided, should be null
////        assertEquals("New York", user.getCity());  // Only city should be updated
//    //}
//        @Test
//    void testUpdateWithOnlyMandatoryFields() throws IOException {
//        String email = "test@example.com";
//        User existingUser = User.builder()
//                .id(1L)
//                .firstName("John")  // Add other required fields if necessary
//                .lastName("Doe")    // Add other required fields if necessary
//                .email(email)
//                .password("oldPassword")
//                .imageUrl("oldImageUrl")
//                .createdAt(new Date())
//                .isEnabled(true)
//                .role("ROLE_USER")
//                .gender("Male")  // Add other required fields if necessary
//                .country("USA")  // Add other required fields if necessary
//                .city("New York") // Add other required fields if necessary
//                .build();
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
//
//        profileService.updateUser(email, email, "John", "Doe", "Male", null, "USA", "New York", null);
//
//        verify(userRepository).save(argThat(user -> user.getFirstName().equals("John")
//                && user.getLastName().equals("Doe")
//                && user.getCity().equals("New York")
//                && user.getCountry().equals("USA")));
//    }
//
//}
