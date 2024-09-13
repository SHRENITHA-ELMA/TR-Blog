package com.epam.travel.management.application.controller;


import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogRequest;
import com.epam.travel.management.application.dto.ViewBlogResponse;
import com.epam.travel.management.application.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BlogControllerTest {

    @Mock
    private BlogService blogService;

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
        when(blogService.createBlog(null, blogRequest)).thenReturn(apiResponse);

        // Act
        ResponseEntity<ApiResponse<Object>> response = blogController.createBlog(null, blogRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }
    @Test
    void getApprovedBlogs_Success() {
        // Arrange
        List<ViewBlogResponse> approvedBlogsList = new ArrayList<>();
        approvedBlogsList.add(ViewBlogResponse.builder()
                .id(1L)
                .title("Sample Blog")
                .content("Sample Content")
                .build());

        ApiResponse<List<ViewBlogResponse>> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Blogs retrieved successfully", approvedBlogsList);
        when(blogService.getApprovedBlogs()).thenReturn(apiResponse);

        ResponseEntity<ApiResponse<List<ViewBlogResponse>>> response = blogController.getApprovedBlogs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }
    @Test
    void getApprovedBlogs_NoBlogsFound() {
        ApiResponse<List<ViewBlogResponse>> apiResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No blogs found", new ArrayList<>());
        when(blogService.getApprovedBlogs()).thenReturn(apiResponse);

        ResponseEntity<ApiResponse<List<ViewBlogResponse>>> response = blogController.getApprovedBlogs();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }
}
