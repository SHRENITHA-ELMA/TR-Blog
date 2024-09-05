package com.epam.travel.management.application.feignClient;

import com.epam.travel.management.application.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-management-service")
public interface UserClient {

    @GetMapping("/users/{userId}")
    Optional<User> getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/users/email/{email}")
    Optional<User> getUserByEmail(@PathVariable("email") String email);
}