package com.epam.travel.management.application.controller;

import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogRequest;
import com.epam.travel.management.application.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createBlog(HttpServletRequest request, @Valid @RequestBody BlogRequest blogRequest) {
        blogService.createBlog(request,blogRequest);
        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Blog has been created successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
