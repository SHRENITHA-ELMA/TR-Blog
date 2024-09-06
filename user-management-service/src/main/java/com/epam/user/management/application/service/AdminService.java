package com.epam.user.management.application.service;
import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import java.util.List;
import java.util.Optional;
public interface AdminService {
    List<UserResponse> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    ApiResponse<Object> setUserEnabledStatus(String email, UserEnableDisableRequest userEnableDisableRequest);
    String create(UserCreateRequest userCreateRequest);
    String updateUser(String email,UserEditRequest userEditRequest);
}
