package com.epam.user.management.application.controller;
import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.AdminServiceImpl;
import com.epam.user.management.application.utility.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;
    @Mock
    private AdminServiceImpl adminServiceImpl;
    @Mock
    private TokenUtils tokenUtils;
    @Mock
    private HttpServletRequest request;
    private User mockUser;
    private UserResponse mockUserResponse;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUserResponse = new UserResponse();
    }
    @Test
    void testGetAllUsers() {
        when(adminServiceImpl.getAllUsers()).thenReturn(List.of(mockUserResponse));
        List<UserResponse> result = adminController.getAllUsers();
        assertEquals(1, result.size());
        verify(adminServiceImpl, times(1)).getAllUsers();
    }
    @Test
    void testSetUserStatus_Success() {
        UserEnableDisableRequest request = new UserEnableDisableRequest(true);
        MessageResponse expectedResponse = new MessageResponse("User enabled successfully");
        when(tokenUtils.getUserFromRequest(any(HttpServletRequest.class))).thenReturn(Optional.of(mockUser));
        when(adminServiceImpl.setUserEnabledStatus(anyLong(), anyString(), any(UserEnableDisableRequest.class)))
                .thenReturn(expectedResponse);
        ResponseEntity<MessageResponse> response = adminController.setUserStatus(1L, request, this.request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
    @Test
    void testSetUserStatus_Forbidden() {
        when(tokenUtils.getUserFromRequest(any(HttpServletRequest.class))).thenReturn(Optional.empty());
        ResponseEntity<MessageResponse> response = adminController.setUserStatus(1L, new UserEnableDisableRequest(true), request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    @Test
    void testCreateUser() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        RegisterResponse registerResponse = new RegisterResponse("User Created successfully");
        when(adminServiceImpl.create(userCreateRequest)).thenReturn(registerResponse);
        ResponseEntity<RegisterResponse> response = adminController.createUser(userCreateRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(registerResponse, response.getBody());
        verify(adminServiceImpl, times(1)).create(userCreateRequest);
    }
    @Test
    void testUpdateUser_Success() {
        UserEditRequest userEditRequest = new UserEditRequest();
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.of(mockUser));
        when(adminServiceImpl.updateUser(mockUser.getEmail(), userEditRequest))
                .thenReturn(new MessageResponse("User updated"));
        ResponseEntity<MessageResponse> response = adminController.updateUser(request, userEditRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User updated", response.getBody().getMessage());
        verify(adminServiceImpl, times(1)).updateUser(mockUser.getEmail(), userEditRequest);
    }
    @Test
    void testUpdateUser_Forbidden() {
        UserEditRequest userEditRequest = new UserEditRequest();
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.empty());
        ResponseEntity<MessageResponse> response = adminController.updateUser(request, userEditRequest);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(adminServiceImpl, times(0)).updateUser(anyString(), any());
    }
}