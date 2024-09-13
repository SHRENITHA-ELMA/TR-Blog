package com.epam.travel.management.application.controller;

import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogRequest;
import com.epam.travel.management.application.dto.ViewBlogRequest;
import com.epam.travel.management.application.dto.ViewBlogResponse;
import com.epam.travel.management.application.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final BlogService blogService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> createBlog(HttpServletRequest request, @Valid @ModelAttribute BlogRequest blogRequest) {
        ApiResponse<Object> response = blogService.createBlog(request, blogRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ViewBlogResponse>>> getApprovedBlogs(){
        ApiResponse<List<ViewBlogResponse>> approvedBlogs = blogService.getApprovedBlogs();
        return ResponseEntity.status(approvedBlogs.getStatus()).body(approvedBlogs);
    }

    @GetMapping("/filters")
    public ResponseEntity<ApiResponse<List<ViewBlogResponse>>> getApprovedBlogsByCountry(
            @RequestParam(required = false) String regionId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String countryId) {
        ViewBlogRequest viewBlogRequest = new ViewBlogRequest(regionId, categoryId,countryId);
        ApiResponse<List<ViewBlogResponse>> approvedCountryBlogs = blogService.getApprovedBlogsByFilters(viewBlogRequest);
        return ResponseEntity.status(approvedCountryBlogs.getStatus()).body(approvedCountryBlogs);
    }
}
