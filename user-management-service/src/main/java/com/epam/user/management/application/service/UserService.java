package com.epam.user.management.application.service;

import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;

import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User getUserById(Long id)
    {
        Optional<User> user =userRepository.findById(id);
        if(user.isPresent())
        {
            return user.get();
        }
        return null;
    }

}
