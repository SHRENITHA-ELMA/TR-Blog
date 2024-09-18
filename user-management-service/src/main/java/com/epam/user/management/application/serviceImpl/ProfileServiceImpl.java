//package com.epam.user.management.application.serviceImpl;
//
//import com.epam.user.management.application.dto.UserResponse;
//import com.epam.user.management.application.entity.User;
//import com.epam.user.management.application.exception.EmailMismatchException;
//import com.epam.user.management.application.exception.InvalidFileFormatException;
//import com.epam.user.management.application.exception.UnauthorizedAccessException;
//import com.epam.user.management.application.exception.UserNotFoundException;
//import com.epam.user.management.application.repository.UserRepository;
//import com.epam.user.management.application.service.FileStorageService;
//import com.epam.user.management.application.service.ProfileService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.java.Log;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.Optional;
//import java.util.regex.Pattern;
//
//@Log
//@Service
//@RequiredArgsConstructor
//public class ProfileServiceImpl implements ProfileService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final ObjectMapper objectMapper;
//    private final FileStorageService fileStorageService;
//    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
//    private static final String[] ALLOWED_CONTENT_TYPES = { "image/jpeg", "image/png","image/jpg" };
//
//    private boolean isPasswordValid(String password) {
//        return Pattern.matches(PASSWORD_PATTERN, password);
//    }
//
//    private boolean isFileTypeValid(MultipartFile file) {
//        if (file == null || file.isEmpty()) {
//            return true;
//        }
//        String contentType = file.getContentType();
//        for (String allowedContentType : ALLOWED_CONTENT_TYPES) {
//            if (allowedContentType.equals(contentType)) {
//                return true;
//            }
//        }
//        return false;
//    }
//    @Override
//    public UserResponse getProfileByUsers(String email) {
//        Optional<User> user = userRepository.findByEmail(email);
//        if (user.isPresent()) {
//            if ("User".equalsIgnoreCase(user.get().getRole())) {
//                log.info(user.get().getImageUrl());
//                return user.map(value -> objectMapper.convertValue(value, UserResponse.class)).orElse(null);
//            } else {
//                throw new UnauthorizedAccessException("Access denied for users with role: " + user.get().getRole());
//            }
//        } else {
//            throw new UserNotFoundException("User not found with email: " + email);
//        }
//    }
//    @Override
//    public void updateUser(String emailFromToken,String email, String firstName, String lastName, String gender, String password, String country, String city, MultipartFile file) throws IOException {
//        if (password != null && !password.isEmpty() && !isPasswordValid(password)) {
//            throw new IllegalArgumentException("Password does not meet the required criteria.");
//        }
//        if(!emailFromToken.equals(email))
//        {
//            throw new EmailMismatchException("Email doesn't match");
//        }
//        if (file != null && !isFileTypeValid(file)) {
//            throw new InvalidFileFormatException("File must be in JPEG,JPG or PNG format");
//        }
//        String filePath = "";
//        if (file != null && !file.isEmpty()) {
//            filePath = fileStorageService.storeFile(file);
//        }
//
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isEmpty()) {
//            throw new UserNotFoundException("User not found with email: " + email);
//        }
//        User user = optionalUser.get();
//        User.UserBuilder userBuilder = User.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .role(user.getRole())
//                .isEnabled(user.isEnabled())
//                .createdAt(user.getCreatedAt())
//                .updatedAt(new Date())  // Set updated time
//                .firstName(firstName)
//                .lastName(lastName)
//                .gender(gender)
//                .city(city)
//                .country(country);
//        // Conditionally update password if it's not null
//        if (password != null && !password.isEmpty()) {
//            userBuilder.password(passwordEncoder.encode(password));
//        } else {
//            userBuilder.password(user.getPassword());  // Retain the existing password if not updating
//        }
//        // Conditionally update imageUrl if filePath is not empty
//        if (!filePath.isEmpty()) {
//            userBuilder.imageUrl(filePath);
//        } else {
//            userBuilder.imageUrl(user.getImageUrl());  // Retain the existing imageUrl if no new file
//        }
//        // Build the updated user object
//        User updatedUser = userBuilder.build();
//        UserResponse userResponse = new UserResponse();
//        userResponse.setFirstName(updatedUser.getFirstName());
//        userResponse.setLastName(updatedUser.getLastName());
//        userResponse.setEmail(updatedUser.getEmail());
//        userResponse.setGender(updatedUser.getGender());
//        userResponse.setCity(updatedUser.getCity());
//        userResponse.setCountry(updatedUser.getCountry());
//        userResponse.setImageUrl(updatedUser.getImageUrl());
//        userRepository.save(updatedUser);
//    }
//}
package com.epam.user.management.application.serviceImpl;

import com.epam.user.management.application.dto.UserProfileRequest;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.EmailMismatchException;
import com.epam.user.management.application.exception.InvalidFileFormatException;
import com.epam.user.management.application.exception.UnauthorizedAccessException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.FileStorageService;
import com.epam.user.management.application.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final FileStorageService fileStorageService;

    private boolean isPasswordValid(String password) {
        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
        return Pattern.matches(PASSWORD_PATTERN, password);
    }
    private boolean isFileTypeValid(MultipartFile file) {
        String[] ALLOWED_CONTENT_TYPES = { "image/jpeg", "image/png","image/jpg" };
        if (file == null || file.isEmpty()) {
            return true; // If the file is not provided, skip validation
        }
        String contentType = file.getContentType();
        for (String allowedContentType : ALLOWED_CONTENT_TYPES) {
            if (allowedContentType.equals(contentType)) {
                return true;
            }
        }
        return false;
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
//    @Override
//    public void updateUser(String emailFromToken, UserProfileRequest userProfileRequest) throws IOException{
//        String email= userProfileRequest.getEmail();
//        String firstName= userProfileRequest.getFirstName();
//        String lastName= userProfileRequest.getLastName();
//        String gender= userProfileRequest.getGender();
//        String password= userProfileRequest.getPassword();
//        String country= userProfileRequest.getCountry();
//        String city= userProfileRequest.getCity();
//        MultipartFile file= userProfileRequest.getFile();
//        if (password != null && !password.isEmpty() && !isPasswordValid(password)) {
//            throw new IllegalArgumentException("Password does not meet the required criteria.");
//        }
//        if(!emailFromToken.equals(email))
//        {
//            throw new EmailMismatchException("Email doesn't match");
//        }
//        if (file != null && !isFileTypeValid(file)) {
//            throw new InvalidFileFormatException("File must be in JPEG,JPG or PNG format");
//        }
//        String filePath = "";
//        if (file != null && !file.isEmpty()) {
//            filePath = fileStorageService.storeFile(file);
//        }
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isEmpty()) {
//            throw new UserNotFoundException("User not found with email: " + email);
//        }
//        User user = optionalUser.get();
//        User.UserBuilder userBuilder = User.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .role(user.getRole())
//                .isEnabled(user.isEnabled())
//                .createdAt(user.getCreatedAt())
//                .updatedAt(new Date())  // Set updated time
//                .firstName(firstName)
//                .lastName(lastName)
//                .gender(gender)
//                .city(city)
//                .country(country);
//        // Conditionally update password if it's not null
//        if (password != null && !password.isEmpty()) {
//            userBuilder.password(passwordEncoder.encode(password));
//        } else {
//            userBuilder.password(user.getPassword());  // Retain the existing password if not updating
//        }
//        // Conditionally update imageUrl if filePath is not empty
//        if (!filePath.isEmpty()) {
//            userBuilder.imageUrl(filePath);
//        } else {
//            userBuilder.imageUrl(user.getImageUrl());  // Retain the existing imageUrl if no new file
//        }
//        // Build the updated user object
//        User updatedUser = userBuilder.build();
//        UserResponse userResponse = new UserResponse();
//        userResponse.setFirstName(updatedUser.getFirstName());
//        userResponse.setLastName(updatedUser.getLastName());
//        userResponse.setEmail(updatedUser.getEmail());
//        userResponse.setGender(updatedUser.getGender());
//        userResponse.setCity(updatedUser.getCity());
//        userResponse.setCountry(updatedUser.getCountry());
//        userResponse.setImageUrl(updatedUser.getImageUrl());
//        userRepository.save(updatedUser);
//    }
    @Override
    public void updateUser(String emailFromToken, UserProfileRequest userProfileRequest) throws IOException {
        validateUserProfileRequest(emailFromToken, userProfileRequest);
        String filePath = handleFileUpload(userProfileRequest.getFile());
        Optional<User> optionalUser = userRepository.findByEmail(userProfileRequest.getEmail());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with email: " + userProfileRequest.getEmail());
        }
        User user = buildUpdatedUser(optionalUser.get(), userProfileRequest, filePath);
        userRepository.save(user);
    }

    private void validateUserProfileRequest(String emailFromToken, UserProfileRequest userProfileRequest) {
        if (!emailFromToken.equals(userProfileRequest.getEmail())) {
            throw new EmailMismatchException("Email doesn't match");
        }
        String password = userProfileRequest.getPassword();
        if (password != null && !password.isEmpty() && !isPasswordValid(password)) {
            throw new IllegalArgumentException("Password does not meet the required criteria.");
        }
        MultipartFile file = userProfileRequest.getFile();
        if (file != null && !isFileTypeValid(file)) {
            throw new InvalidFileFormatException("File must be in JPEG, JPG or PNG format");
        }
    }

    private String handleFileUpload(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            return fileStorageService.storeFile(file);
        }
        return "";
    }

    private User buildUpdatedUser(User user, UserProfileRequest request, String filePath) {
        User.UserBuilder userBuilder = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .isEnabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(new Date())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .city(request.getCity())
                .country(request.getCountry());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            userBuilder.password(passwordEncoder.encode(request.getPassword()));
        } else {
            userBuilder.password(user.getPassword());
        }
        if (!filePath.isEmpty()) {
            userBuilder.imageUrl(filePath);
        } else {
            userBuilder.imageUrl(user.getImageUrl());
        }
        return userBuilder.build();
    }
}