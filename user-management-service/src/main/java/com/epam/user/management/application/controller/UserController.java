package com.epam.user.management.application.controller;

import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable Long userId)
    {
        return userService.getUserById(userId);
    }
}
