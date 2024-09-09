package com.admin_management_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVICE",path = "/users")
public interface CountryFeign {
    @GetMapping("/verify")
    public ResponseEntity<?> verifyAdmin(@RequestParam("token") String token);
}
