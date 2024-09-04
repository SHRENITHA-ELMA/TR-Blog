package com.epam.travel.management.application.feignClient;

import com.epam.travel.management.application.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-management-service", url = "${user.management.service.url}")
public interface UserClient {

    @GetMapping("/users/{userId}")
    User getUserById(@PathVariable("userId") Long userId);
}