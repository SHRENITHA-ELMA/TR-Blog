package com.epam.user.management.application.controller;
import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UserForbiddenException;

import com.epam.user.management.application.serviceImpl.AdminServiceImpl;
import com.epam.user.management.application.utility.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@RestController
@RequestMapping("/admin")
@Validated
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServiceImpl adminServiceImpl;
    private final TokenUtils tokenUtils;
    public void ensureAdminAccess(HttpServletRequest request) {
        Optional<User> currentUserOpt = tokenUtils.getUserFromRequest(request);
        if (currentUserOpt.isPresent() && !Objects.equals(currentUserOpt.get().getRole(), "Admin")) {
            throw new UserForbiddenException("You are not authorized for this action");
        }
    }
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(HttpServletRequest request) {
        ensureAdminAccess(request);
        List<UserResponse> response = adminServiceImpl.getAllUsers();
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(response)
                .build());
    }

    @PutMapping("/users/changeStatus")
    public ResponseEntity<ApiResponse<Object>> setUserStatus(@Valid @RequestBody UserEnableDisableRequest userEnableDisableRequest,
                                                             HttpServletRequest request) {
        ensureAdminAccess(request);
        User currentUser = tokenUtils.getUserFromRequest(request).orElseThrow(); // Assumes admin access is ensured
        ApiResponse<Object> response = adminServiceImpl.setUserEnabledStatus(currentUser.getEmail(), userEnableDisableRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<Object>> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest,
                                                          HttpServletRequest request) {
        ensureAdminAccess(request);
        String response = adminServiceImpl.create(userCreateRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(response)
                .build());
    }
    @PutMapping("/users")
    public ResponseEntity<ApiResponse<Object>> updateUser(@Valid @RequestBody UserEditRequest userEditRequest,
                                                          HttpServletRequest request) {
        ensureAdminAccess(request);
        User currentUser = tokenUtils.getUserFromRequest(request).orElseThrow(); // Assumes admin access is ensured
        String response = adminServiceImpl.updateUser(userEditRequest.getEmail(), userEditRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(response)
                .build());
    }
}
