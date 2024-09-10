package com.epam.travel.management.application.feign;

import com.epam.travel.management.application.entity.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVICE", path = "/users")
public interface UserClient {
    @GetMapping
    public ResponseEntity<UserResponse> getUserFromToken(@RequestParam String token);
}
