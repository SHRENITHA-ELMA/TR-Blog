package com.epam.user.management.application.serviceImpl;
import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.AuthorizationException;
import com.epam.user.management.application.exception.UserAlreadyExistsException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public List<UserResponse> getAllUsers() {
        try {
            List<User> users = userRepository.findByRole("User");
            if (users.isEmpty()) {
                throw new UserNotFoundException("Empty data in database.");
            }
            return users.stream()
                    .map(user -> {
                        UserResponse response = new UserResponse();
                        response.setId(user.getId());
                        response.setEmail(user.getUsername());
                        response.setFirstName(user.getFirstName());
                        response.setLastName(user.getLastName());
                        response.setGender(user.getGender());
                        response.setCountry(user.getCountry());
                        response.setCity(user.getCity());
                        response.setEnabled(user.isEnabled());
                        return response;
                    })
                    .toList();
        } catch (UserNotFoundException e) {
            throw e;
        }
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public ApiResponse<Object> setUserEnabledStatus(String email, UserEnableDisableRequest request) {
        User requestingUser = getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Current user not found."));
        User targetUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + request.getId()));
        if (!"Admin".equals(requestingUser.getRole())) {
            throw new AuthorizationException("You are not authorized to perform this action.");
        }
        targetUser.setEnabled(request.getEnable());
        userRepository.save(targetUser);
        String action = Boolean.TRUE.equals(request.getEnable()) ? "enabled" : "disabled";
        ApiResponse<Object> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("User " + action + " successfully");
        return response;
    }
    public String create(UserCreateRequest userCreateRequest) {
        try {
            Optional<User> user = userRepository.findByEmail(userCreateRequest.getEmail());
            if (user.isPresent()) {
                throw new UserAlreadyExistsException("User with email already exists.");
            }
            User newUser = User.builder()
                    .firstName(userCreateRequest.getFirstName())
                    .lastName(userCreateRequest.getLastName())
                    .email(userCreateRequest.getEmail())
                    .password(passwordEncoder.encode(userCreateRequest.getPassword()))
                    .gender(userCreateRequest.getGender())
                    .country(userCreateRequest.getCountry())
                    .city(userCreateRequest.getCity())
                    .role(userCreateRequest.getRole())
                    .isEnabled(true)
                    .build();
            userRepository.save(newUser);
            return "User Created successfully";
        } catch (UserAlreadyExistsException e) {
            throw e;
        }
    }
    public String updateUser(String targetUserEmail, UserEditRequest userEditRequest) {
        try {
            if (targetUserEmail == null || userEditRequest == null) {
                throw new IllegalArgumentException("Email and user edit request must not be null.");
            }
            Optional<User> userOptional = getUserByEmail(targetUserEmail);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFirstName(userEditRequest.getFirstName());
                user.setLastName(userEditRequest.getLastName());
                user.setGender(userEditRequest.getGender());
                user.setCountry(userEditRequest.getCountry());
                user.setCity(userEditRequest.getCity());
                user.setRole("User");
                userRepository.save(user);
                return "User updated successfully";
            } else {
                throw new UserNotFoundException("User not found with email: " + targetUserEmail);
            }
        } catch (IllegalArgumentException | UserNotFoundException e) {
            throw e;
        }
    }
}
