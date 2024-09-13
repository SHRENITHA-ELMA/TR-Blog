package com.admin_management_service.controller;

import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.entity.Blog;
import com.admin_management_service.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-blog")
public class BlogController {
    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Blog>>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogs = blogService.getAllBlogs(pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Blog>>builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(blogs)
                .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> enableOrDisableBlog(@PathVariable Long id, @RequestParam boolean enabled) {
        blogService.enableOrDisableBlog(id, enabled);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
