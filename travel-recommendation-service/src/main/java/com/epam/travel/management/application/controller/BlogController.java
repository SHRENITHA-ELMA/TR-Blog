package com.epam.travel.management.application.controller;

import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.service.BlogServiceImpl;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    private final BlogServiceImpl blogService;

    public BlogController(BlogServiceImpl blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/view")
    public List<Blog> getAllBlogs()
    {
        return blogService.getAllBlogs();
    }

}
