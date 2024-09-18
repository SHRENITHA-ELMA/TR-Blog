package com.epam.travel.management.application.controller;

import com.epam.travel.management.application.dto.*;
import com.epam.travel.management.application.exceptions.InvalidStatusException;
import com.epam.travel.management.application.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BlogControllerTest {

    @Mock
    private BlogService blogService;
    @Mock
    private HttpServletRequest httpServletRequest;
    @InjectMocks
    private BlogController blogController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBlog_Success() {
        // Arrange
        BlogRequest blogRequest = BlogRequest.builder()
                .title("Test Title")
                .content("Test Content")
                .image(null)
                .regionId("1")
                .countryId("1")
                .categoryId("1")
                .build();
        ApiResponse<Object> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), "Blog created", null);
        when(blogService.createBlog(httpServletRequest, blogRequest)).thenReturn(apiResponse);
        // Act
        ResponseEntity<ApiResponse<Object>> response = blogController.createBlog(httpServletRequest, blogRequest);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }

    @Test
    void getAllBlogs_Success() {
        // Arrange
        BlogResponse blogResponse = new BlogResponse(); // Populate as needed
        ApiResponse<BlogResponse> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Blogs fetched", blogResponse);
        when(blogService.getAllBlogs()).thenReturn(apiResponse);
        // Act
        ResponseEntity<ApiResponse<BlogResponse>> response = blogController.getAllBlogs();
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }

    @Test
    void getFilteredBlogs_Success() {
        // Arrange
        String categoryId = "1";
        String regionId = "1";
        String countryId = "1";
        BlogResponse blogResponse = new BlogResponse(); // Populate as needed
        ApiResponse<BlogResponse> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Blogs fetched", blogResponse);
        when(blogService.getFilteredBlogs(categoryId, regionId, countryId)).thenReturn(apiResponse);
        // Act
        ResponseEntity<ApiResponse<BlogResponse>> response = blogController.getFilteredBlogs(categoryId, regionId, countryId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }

    @Test
    void updateBlogStatus_Success() {
        // Arrange
        BlogStatusUpdateRequest updateRequest = new BlogStatusUpdateRequest(1L, "ACTIVE");
        ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Blog status updated successfully!", null);
        doNothing().when(blogService).updateBlogStatus(updateRequest.getId(), updateRequest.getStatus());
        // Act
        ResponseEntity<ApiResponse<String>> response = blogController.updateBlogStatus(httpServletRequest, updateRequest);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }

    @Test
    void updateBlogStatus_InvalidStatus() {
        // Arrange
        BlogStatusUpdateRequest updateRequest = new BlogStatusUpdateRequest(1L, "INVALID_STATUS");
        ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid status. Only 'accept' or 'reject' are allowed.", null);
        doThrow(new InvalidStatusException("Invalid status. Only 'accept' or 'reject' are allowed.")).when(blogService).updateBlogStatus(updateRequest.getId(), updateRequest.getStatus());
        // Act
        ResponseEntity<ApiResponse<String>> response = blogController.updateBlogStatus(httpServletRequest, updateRequest);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }
    @Test
    void updateBlogStatus_InternalServerError() {
        // Arrange
        BlogStatusUpdateRequest updateRequest = new BlogStatusUpdateRequest(1L, "ACTIVE");
        ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update blog status.", null);
        doThrow(new RuntimeException("Invalid status. Only 'accept' or 'reject' are allowed.")).when(blogService).updateBlogStatus(updateRequest.getId(), updateRequest.getStatus());
        // Act
        ResponseEntity<ApiResponse<String>> response = blogController.updateBlogStatus(httpServletRequest, updateRequest);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }
}
