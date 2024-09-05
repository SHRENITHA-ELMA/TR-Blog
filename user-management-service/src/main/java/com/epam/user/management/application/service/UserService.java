package com.epam.user.management.application.service;

import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<User> getUserById(Long id)
    {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

}
