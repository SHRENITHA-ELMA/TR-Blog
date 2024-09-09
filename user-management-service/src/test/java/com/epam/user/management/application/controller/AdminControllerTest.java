package com.epam.user.management.application.controller;
import com.epam.user.management.application.dto.UserCreateRequest;
import com.epam.user.management.application.dto.UserEditRequest;
import com.epam.user.management.application.dto.UserEnableDisableRequest;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UserForbiddenException;

import com.epam.user.management.application.serviceImpl.AdminServiceImpl;
import com.epam.user.management.application.utility.TokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Mock
    private AdminServiceImpl adminServiceImpl;
    @Mock
    private TokenUtils tokenUtils;
    @InjectMocks
    private AdminController adminController;
    private MockHttpServletRequest request;
    private User adminUser;
    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setRole("Admin");
    }
    @Test
    void ensureAdminAccess_ShouldThrowUserForbiddenException_WhenUserIsNotAdmin() {
        User nonAdminUser = new User();
        nonAdminUser.setRole("User");
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.of(nonAdminUser));
        assertThrows(UserForbiddenException.class, () -> {
            adminController.ensureAdminAccess(request);
        });
    }
    @Test
    void ensureAdminAccess_ShouldNotThrowException_WhenUserIsAdmin() {
        User adminuser=new User();
        adminuser.setRole("Admin");
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.of(adminuser));
        adminController.ensureAdminAccess(request);
    }
    @Test
    void ensureAdminAccess_ShouldNotThrowException_WhenNoUserPresent() {
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.empty());
        adminController.ensureAdminAccess(request);
    }
    @Test
    void getAllUsers_AdminAccess_Success() {
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.of(adminUser));
        adminController.getAllUsers(request);
        verify(adminServiceImpl).getAllUsers();
    }
    @Test
    void getAllUsers_NoAdminAccess_Forbidden() {
        User regularUser = new User();
        regularUser.setRole("User");
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.of(regularUser));
        assertThrows(UserForbiddenException.class, () -> adminController.getAllUsers(request));
    }
    @Test
    void setUserStatus_AdminAccess_Success() {
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.of(adminUser));
        UserEnableDisableRequest requestDto = new UserEnableDisableRequest(1L, true);
        adminController.setUserStatus(requestDto, request);
        verify(adminServiceImpl).setUserEnabledStatus(adminUser.getEmail(), requestDto);
    }
    @Test
    void createUser_AdminAccess_Success() {
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.of(adminUser));
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail("newuser@example.com");
        userCreateRequest.setRole("User");
        adminController.createUser(userCreateRequest, request);
        verify(adminServiceImpl).create(userCreateRequest);
    }
    @Test
    void updateUser_AdminAccess_Success() {
        when(tokenUtils.getUserFromRequest(request)).thenReturn(Optional.of(adminUser));
        UserEditRequest userEditRequest = new UserEditRequest();
        userEditRequest.setEmail("user@example.com");
        userEditRequest.setCountry("New Country");
        adminController.updateUser(userEditRequest, request);
        verify(adminServiceImpl).updateUser(userEditRequest.getEmail(), userEditRequest);
    }
}