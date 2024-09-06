package com.epam.user.management.application.serviceImpl;

import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UnauthorizedAccessException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.FileStorageService;
import com.epam.user.management.application.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Log
@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    private final FileStorageService fileStorageService;

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";


    public ProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ObjectMapper objectMapper, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.fileStorageService = fileStorageService;
    }

    private boolean isPasswordValid(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

    @Override
    public UserResponse getProfileByUsers(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if ("User".equalsIgnoreCase(user.get().getRole())) {
                log.info(user.get().getImageUrl());
                return user.map(value -> objectMapper.convertValue(value, UserResponse.class)).orElse(null);


            } else {
                throw new UnauthorizedAccessException("Access denied for users with role: " + user.get().getRole());
            }
        } else {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }
    @Override
    public void updateUser(String email, String firstName, String lastName, String gender, String password, String country, String city, MultipartFile file) throws IOException {
        if (password != null && !password.isEmpty() && !isPasswordValid(password)) {
            throw new IllegalArgumentException("Password does not meet the required criteria.");
        }

        String filePath = "";
        if (file != null && !file.isEmpty()) {
            filePath = fileStorageService.storeFile(file);
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with email: " + email);
        }

        User user = optionalUser.get();

        User.UserBuilder userBuilder = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .isEnabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(new Date())  // Set updated time
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .city(city)
                .country(country);

        // Conditionally update password if it's not null
        if (password != null && !password.isEmpty()) {
            userBuilder.password(passwordEncoder.encode(password));
        } else {
            userBuilder.password(user.getPassword());  // Retain the existing password if not updating
        }

        // Conditionally update imageUrl if filePath is not empty
        if (!filePath.isEmpty()) {
            userBuilder.imageUrl(filePath);
        } else {
            userBuilder.imageUrl(user.getImageUrl());  // Retain the existing imageUrl if no new file
        }
        System.out.println(user.getImageUrl());
        // Build the updated user object
        User updatedUser = userBuilder.build();
        System.out.println(updatedUser.getImageUrl());

        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName(updatedUser.getFirstName());
        userResponse.setLastName(updatedUser.getLastName());
        userResponse.setEmail(updatedUser.getEmail());
        userResponse.setGender(updatedUser.getGender());
        userResponse.setCity(updatedUser.getCity());
        userResponse.setCountry(updatedUser.getCountry());
        userResponse.setImageUrl(updatedUser.getImageUrl());

        userRepository.save(updatedUser);
    }

}