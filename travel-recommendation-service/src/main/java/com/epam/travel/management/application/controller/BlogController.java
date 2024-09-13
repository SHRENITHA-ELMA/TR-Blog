package com.epam.travel.management.application.controller;

import com.epam.travel.management.application.dto.*;
import com.epam.travel.management.application.exceptions.InvalidStatusException;
import com.epam.travel.management.application.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    @GetMapping("/All")
    public ResponseEntity<ApiResponse<BlogResponse>> getAllBlogs()
    {
        ApiResponse<BlogResponse> response = blogService.getAllBlogs();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<BlogResponse>> getFilteredBlogs(@RequestBody AdminBlogFilterRequest adminBlogFilterRequest)
    {
        ApiResponse<BlogResponse> response=blogService.getFilteredBlogs(adminBlogFilterRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/status")
    public ResponseEntity<ApiResponse<String>> updateBlogStatus(HttpServletRequest request,
            @RequestBody BlogStatusUpdateRequest blogStatusUpdateRequest) {
        try {
            blogService.updateBlogStatus(blogStatusUpdateRequest.getId(), blogStatusUpdateRequest.getStatus());
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Blog status updated successfully!", null));
        } catch (InvalidStatusException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update blog status.", null));
        }
    }
}
