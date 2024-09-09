package com.epam.user.management.application.serviceImpl;
import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.AuthorizationException;
import com.epam.user.management.application.exception.UserAlreadyExistsException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class AdminServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AdminServiceImpl adminService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAllUsers_ShouldReturnUserResponseList_WhenUsersExist() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setGender("Male");
        user.setCountry("Country");
        user.setCity("City");
        user.setEnabled(true);
        when(userRepository.findByRole("User")).thenReturn(List.of(user));
        List<UserResponse> userResponses = adminService.getAllUsers();
        assertFalse(userResponses.isEmpty());
        assertEquals(1, userResponses.size());
        assertEquals("test@example.com", userResponses.get(0).getEmail());
    }
    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> result = adminService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("user@example.com", result.get().getEmail());
    }
    @Test
    void getUserById_ShouldReturnEmptyOptional_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<User> result = adminService.getUserById(1L);
        assertFalse(result.isPresent());
    }
    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() {
        User user = new User();
        user.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Optional<User> result = adminService.getUserByEmail("user@example.com");
        assertTrue(result.isPresent());
        assertEquals("user@example.com", result.get().getEmail());
    }
    @Test
    void getUserByEmail_ShouldReturnEmptyOptional_WhenUserDoesNotExist() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        Optional<User> result = adminService.getUserByEmail("user@example.com");
        assertFalse(result.isPresent());
    }
    @Test
    void getAllUsers_ShouldThrowUserNotFoundException_WhenNoUsersExist() {
        when(userRepository.findByRole("User")).thenReturn(List.of());
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            adminService.getAllUsers();
        });
        assertEquals("Empty data in database.", thrown.getMessage());
    }
    @Test
    void testEnableUser_Success() {
        String email = "admin@example.com";
        User adminUser = new User();
        adminUser.setRole("Admin");
        User targetUser = new User();
        targetUser.setEnabled(false);
        UserEnableDisableRequest request = new UserEnableDisableRequest(1L, true);
        when(adminService.getUserByEmail(email)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(request.getId())).thenReturn(Optional.of(targetUser));
        ApiResponse<Object> response = adminService.setUserEnabledStatus(email, request);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("User enabled successfully", response.getMessage());
        assertTrue(targetUser.isEnabled());
        verify(userRepository, times(1)).save(targetUser);
    }
    @Test
    void testDisableUser_Success() {
        String email = "admin@example.com";
        User adminUser = new User();
        adminUser.setRole("Admin");
        User targetUser = new User();
        targetUser.setEnabled(true);
        UserEnableDisableRequest request = new UserEnableDisableRequest(1L, false);
        when(adminService.getUserByEmail(email)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(request.getId())).thenReturn(Optional.of(targetUser));
        ApiResponse<Object> response = adminService.setUserEnabledStatus(email, request);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("User disabled successfully", response.getMessage());
        assertFalse(targetUser.isEnabled());
        verify(userRepository, times(1)).save(targetUser);
    }
    @Test
    void testEnableDisableUser_UserNotFound() {
        String email = "admin@example.com";
        User adminUser = new User();
        adminUser.setRole("Admin");
        UserEnableDisableRequest request = new UserEnableDisableRequest(1L, true);
        when(adminService.getUserByEmail(email)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(request.getId())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            adminService.setUserEnabledStatus(email, request);
        });
        assertEquals("User not found for ID: 1", exception.getMessage());
    }
    @Test
    void testEnableDisableUser_NotAdmin() {
        String email = "user@example.com";
        User nonAdminUser = new User();
        nonAdminUser.setRole("User");
        UserEnableDisableRequest request = new UserEnableDisableRequest(1L, true);
        when(adminService.getUserByEmail(email)).thenReturn(Optional.of(nonAdminUser));
        when(userRepository.findById(request.getId())).thenReturn(Optional.of(new User()));
        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> {
            adminService.setUserEnabledStatus(email, request);
        });
        assertEquals("You are not authorized to perform this action.", exception.getMessage());
    }
    @Test
    void setUserEnabledStatus_ShouldThrowUserNotFoundException_WhenTargetUserNotFound() {
        String email = "admin@example.com";
        User adminUser = new User();
        adminUser.setRole("Admin");
        UserEnableDisableRequest request = new UserEnableDisableRequest(1L, true);
        when(adminService.getUserByEmail(email)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(request.getId())).thenReturn(Optional.empty());
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            adminService.setUserEnabledStatus(email, request);
        });
        assertEquals("User not found for ID: 1", thrown.getMessage());
    }
    @Test
    void setUserEnabledStatus_ShouldThrowAuthorizationException_WhenCurrentUserIsNotAdmin() {
        String email = "user@example.com";
        User nonAdminUser = new User();
        nonAdminUser.setRole("User");
        User targetUser = new User();
        targetUser.setEnabled(false);
        UserEnableDisableRequest request = new UserEnableDisableRequest(1L, false);
        when(adminService.getUserByEmail(email)).thenReturn(Optional.of(nonAdminUser));
        when(userRepository.findById(request.getId())).thenReturn(Optional.of(targetUser));
        AuthorizationException thrown = assertThrows(AuthorizationException.class, () -> {
            adminService.setUserEnabledStatus(email, request);
        });
        assertEquals("You are not authorized to perform this action.", thrown.getMessage());
    }
    @Test
    void setUserEnabledStatus_ShouldThrowUserNotFoundException_WhenCurrentUserNotFound() {
        String email = "nonexistent@example.com";
        UserEnableDisableRequest request = new UserEnableDisableRequest(1L, true);
        when(adminService.getUserByEmail(email)).thenReturn(Optional.empty());
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            adminService.setUserEnabledStatus(email, request);
        });
        assertEquals("Current user not found.", thrown.getMessage());
    }
    @Test
    void create_ShouldReturnSuccessMessage_WhenUserIsCreated() {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password");
        request.setGender("Male");
        request.setCountry("Country");
        request.setCity("City");
        request.setRole("User");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        String message = adminService.create(request);
        assertEquals("User Created successfully", message);
        verify(userRepository).save(any(User.class));
    }
    @Test
    void create_ShouldThrowUserAlreadyExistsException_WhenUserExists() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("john.doe@example.com");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(new User()));
        UserAlreadyExistsException thrown = assertThrows(UserAlreadyExistsException.class, () -> {
            adminService.create(request);
        });
        assertEquals("User with email already exists.", thrown.getMessage());
    }
    @Test
    void updateUser_ShouldThrowIllegalArgumentException_WhenUserEditRequestIsNull() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateUser("user@example.com", null);
        });
        assertEquals("Email and user edit request must not be null.", thrown.getMessage());
    }
    @Test
    void updateUser_ShouldReturnSuccessMessage_WhenUserIsUpdated() {
        UserEditRequest request = new UserEditRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setGender("Male");
        request.setCountry("Country");
        request.setCity("City");
        User user = new User();
        user.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        String message = adminService.updateUser("user@example.com", request);
        assertEquals("User updated successfully", message);
        verify(userRepository).save(any(User.class));
    }
    @Test
    void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        UserEditRequest request = new UserEditRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            adminService.updateUser("nonexistent@example.com", request);
        });
        assertEquals("User not found with email: nonexistent@example.com", thrown.getMessage());
    }
    @Test
    void setUserEnabledStatus_ShouldThrowUserNotFoundException_WhenCurrentAdminNotFound() {
        UserEnableDisableRequest request = new UserEnableDisableRequest();
        request.setId(1L);
        request.setEnable(true);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            adminService.setUserEnabledStatus("admin@example.com", request);
        });
        assertEquals("Current user not found.", thrown.getMessage());
    }
    @Test
    void testUpdateUser_NullEmail_ThrowsIllegalArgumentException() {
        String targetUserEmail = null;
        UserEditRequest userEditRequest = new UserEditRequest();
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateUser(targetUserEmail, userEditRequest);
        });
    }
    @Test
    void testUpdateUser_NullUserEditRequest_ThrowsIllegalArgumentException() {
        String targetUserEmail = "test@example.com";
        UserEditRequest userEditRequest = null;
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateUser(targetUserEmail, userEditRequest);
        });
    }
}
