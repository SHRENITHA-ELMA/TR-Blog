package com.epam.user.management.application.serviceImpl;

import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.UserServiceOwn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServiceOwn {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isAdmin(String email) {
        User user = userRepository.findByEmail(email).get();
        return user != null && user.getRole().equals("Admin");
    }


    @Override
    public UserResponse getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imageUrl(user.getImageUrl())
                .build();
    }

}

