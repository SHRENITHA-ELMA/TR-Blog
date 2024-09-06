package com.epam.user.management.application.controller;
import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UserForbiddenException;
import com.epam.user.management.application.service.AdminServiceImpl;
import com.epam.user.management.application.utility.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>>  getAllUsers(HttpServletRequest request) {
        Optional<User> currentUserOpt = tokenUtils.getUserFromRequest(request);
        if (currentUserOpt.isPresent() && !Objects.equals(currentUserOpt.get().getRole(), "Admin")) {
            throw new UserForbiddenException("You are not authorized for this action");
        }
        List<UserResponse> response = adminServiceImpl.getAllUsers();
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder().status(HttpStatus.OK.value()).message("Success").data(response).build());
    }

    @PutMapping("/users/changeStatus")
    public ResponseEntity<ApiResponse<Object>> setUserStatus(@Valid @RequestBody UserEnableDisableRequest userEnableDisableRequest, HttpServletRequest request) {
        Optional<User> currentUserOpt = tokenUtils.getUserFromRequest(request);
        if (currentUserOpt.isPresent() && !Objects.equals(currentUserOpt.get().getRole(), "Admin")) {
            throw new UserForbiddenException("You are not authorized for this action");
        }
        User currentUser = currentUserOpt.get();
        ApiResponse<Object> response = adminServiceImpl.setUserEnabledStatus( currentUser.getEmail(), userEnableDisableRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/users/create")
    public ResponseEntity<ApiResponse<Object>> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest, HttpServletRequest request) {
        Optional<User> currentUserOpt = tokenUtils.getUserFromRequest(request);
        if (currentUserOpt.isPresent() && !Objects.equals(currentUserOpt.get().getRole(), "Admin")) {
            throw new UserForbiddenException("You are not authorized for this action");
        }
        String response = adminServiceImpl.create(userCreateRequest);
        return ResponseEntity.ok(ApiResponse.builder().status(HttpStatus.OK.value()).message(response).build());
    }
    @PutMapping("/users/update")
    public ResponseEntity<ApiResponse<Object>> updateUser(HttpServletRequest request,@Valid @RequestBody UserEditRequest userEditRequest) {
        Optional<User> currentUserOpt = tokenUtils.getUserFromRequest(request);
        if (currentUserOpt.isPresent() && !Objects.equals(currentUserOpt.get().getRole(), "Admin")) {
            throw new UserForbiddenException("You are not authorized for this action");
        }
        String response = adminServiceImpl.updateUser(currentUserOpt.get().getEmail(), userEditRequest);
        return ResponseEntity.ok(ApiResponse.builder().status(HttpStatus.OK.value()).message(response).build());
    }
}