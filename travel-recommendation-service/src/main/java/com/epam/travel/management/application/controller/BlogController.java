package com.epam.travel.management.application.controller;

import com.epam.travel.management.application.dto.BlogRequest;
import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.entity.User;
import com.epam.travel.management.application.service.BlogServiceImpl;
import com.epam.travel.management.application.utility.TokenUtils;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/blog")
@Log
public class BlogController {

    private final BlogServiceImpl blogService;
    private final TokenUtils tokenUtils;

    public BlogController(BlogServiceImpl blogService, TokenUtils tokenUtils) {
        this.blogService = blogService;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping("/view")
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @PostMapping("/create")
    public ResponseEntity<Blog> createBlog(HttpServletRequest request, @RequestBody BlogRequest blogRequest) {

        try {

            Optional<User> user = tokenUtils.getUserFromRequest(request);
            return ResponseEntity.ok(blogService.createBlog(blogRequest.getTitle(), blogRequest.getContent(), blogRequest.getImages(), blogRequest.getCity(), blogRequest.getCountry(), user));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }


}
