package com.epam.user.management.application.controller;

import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.UserService;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Log
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userId}")
    public Optional<User> getUserById(@PathVariable Long userId)
    {
        return userService.getUserById(userId);
    }

    @GetMapping("/users/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email)
    {
        log.info(email);
        System.out.println(email);
        return userService.getUserByEmail(email);
    }
}
