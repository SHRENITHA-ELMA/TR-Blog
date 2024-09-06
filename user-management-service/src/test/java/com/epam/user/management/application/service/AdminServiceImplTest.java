package com.epam.user.management.application;
import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.AuthorizationException;
import com.epam.user.management.application.exception.UserAlreadyExistsException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
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
    void testGetAllUsers_Success() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        users.add(user);
        when(userRepository.findByRole("User")).thenReturn(users);
        List<UserResponse> response = adminService.getAllUsers();
        assertEquals(1, response.size());
        assertEquals("test@example.com", response.get(0).getEmail());
    }
    @Test
    void testGetAllUsers_Empty() {
        when(userRepository.findByRole("User")).thenReturn(new ArrayList<>());
        assertThrows(UserNotFoundException.class, () -> adminService.getAllUsers());
    }

    @Test
    void testGetUserById_Success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> result = adminService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }
    @Test
    void testGetUserByEmail_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        Optional<User> result = adminService.getUserByEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getUsername());
    }
    @Test
    public void testSetUserEnabledStatus_Success() {
        User admin = new User();
        admin.setEmail("admin@example.com");
        admin.setRole("Admin");
        User user = new User();
        user.setId(1L);
        user.setEnabled(false);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserEnableDisableRequest request = new UserEnableDisableRequest();
        request.setEnable(true);
        MessageResponse response = adminService.setUserEnabledStatus(1L, "admin@example.com", request);
        assertEquals("User enabled successfully", response.getMessage());
        verify(userRepository, times(1)).save(user);
    }
    @Test
    public void testSetUserEnabledStatus_NotAuthorized() {
        User admin = new User();
        admin.setEmail("admin@example.com");
        admin.setRole("User");
        User user = new User();
        user.setId(1L);
        user.setEnabled(false);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserEnableDisableRequest request = new UserEnableDisableRequest();
        request.setEnable(true);
        assertThrows(AuthorizationException.class, () ->
                adminService.setUserEnabledStatus(1L, "admin@example.com", request));
    }
    @Test
    void testCreateUser_Success() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("password");
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        RegisterResponse response = adminService.create(request);
        assertEquals("User Created successfully", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void testCreateUser_UserAlreadyExists() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("existinguser@example.com");
        when(userRepository.findByEmail("existinguser@example.com")).thenReturn(Optional.of(new User()));
        assertThrows(UserAlreadyExistsException.class, () -> adminService.create(request));
    }
    @Test
    void testUpdateUser_Success() {
        UserEditRequest request = new UserEditRequest();
        request.setFirstName("Updated");
        request.setLastName("User");
        User user = new User();
        user.setEmail("targetuser@example.com");
        when(userRepository.findByEmail("targetuser@example.com")).thenReturn(Optional.of(user));
        MessageResponse response = adminService.updateUser("targetuser@example.com", request);
        assertEquals("User updated successfully", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void testUpdateUser_UserNotFound() {
        UserEditRequest request = new UserEditRequest();
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> adminService.updateUser("nonexistent@example.com", request));
    }
}
