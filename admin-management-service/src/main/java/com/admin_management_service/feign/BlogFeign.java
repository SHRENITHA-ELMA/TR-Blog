package com.admin_management_service.feign;

import com.admin_management_service.entity.Blog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient
public interface BlogFeign {

    @GetMapping("/blogs")
    public ResponseEntity<Blog>getAllBlogs();
}
