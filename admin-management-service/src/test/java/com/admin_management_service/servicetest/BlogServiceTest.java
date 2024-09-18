package com.admin_management_service.servicetest;

import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.BlogResponse;
import com.admin_management_service.dto.BlogStatusUpdateRequest;
import com.admin_management_service.exceptions.InvalidStatusException;
import com.admin_management_service.feign.BlogFeign;
import com.admin_management_service.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlogServiceTest {

    @Mock
    private BlogFeign blogFeign;
    @InjectMocks
    private BlogService blogService;
    private ApiResponse<BlogResponse> blogResponse;
    private ApiResponse<String> statusResponse;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        blogResponse = ApiResponse.<BlogResponse>builder()
                .status(HttpStatus.OK.value())
                .message("All blogs fetched successfully!")
                .data(new BlogResponse())
                .build();
        statusResponse = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Blog status updated successfully!")
                .data(null)
                .build();
    }

    @Test
    void testGetAllBlogs_WithBlogs() {
        // Mocking the Feign client response
        when(blogFeign.getAllBlogs()).thenReturn(ResponseEntity.ok(blogResponse));
        // Call the service method
        ApiResponse<BlogResponse> response = blogService.getAllBlogs();
        // Verify the result
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("All blogs fetched successfully!", response.getMessage());
        assertNotNull(response.getData());
        // Verify the interaction with the Feign client
        verify(blogFeign, times(1)).getAllBlogs();
    }
    @Test
    void testGetAllBlogs_NoBlogs() {
        // Mock Feign response with null data
        ApiResponse<BlogResponse> emptyResponse = ApiResponse.<BlogResponse>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("No blogs found.")
                .data(null)
                .build();
        when(blogFeign.getAllBlogs()).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).body(emptyResponse));
        // Call the service method
        ApiResponse<BlogResponse> response = blogService.getAllBlogs();
        // Verify the result
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("No blogs found.", response.getMessage());
        assertNull(response.getData());
        // Verify the interaction with the Feign client
        verify(blogFeign, times(1)).getAllBlogs();
    }

    @Test
    void testUpdateBlogStatus_Success() {
        // Mock Feign client response
        doNothing().when(blogFeign).updateBlogStatus(any(BlogStatusUpdateRequest.class));
        // Call the service method
        BlogStatusUpdateRequest request = new BlogStatusUpdateRequest(123L, "APPROVED");
        ApiResponse<String> response = blogService.updateBlogStatus(request);
        // Verify the result
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Blog status updated successfully!", response.getMessage());
        // Verify the interaction with the Feign client
        verify(blogFeign, times(1)).updateBlogStatus(any(BlogStatusUpdateRequest.class));
    }

    @Test
    void testUpdateBlogStatus_InvalidStatusException() {
        // Mock an InvalidStatusException
        doThrow(new InvalidStatusException("Invalid status")).when(blogFeign).updateBlogStatus(any(BlogStatusUpdateRequest.class));
        // Call the service method
        BlogStatusUpdateRequest request = new BlogStatusUpdateRequest(123L, "INVALID");
        ApiResponse<String> response = blogService.updateBlogStatus(request);
        // Verify the result
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Invalid status", response.getMessage());
        // Verify the interaction with the Feign client
        verify(blogFeign, times(1)).updateBlogStatus(any(BlogStatusUpdateRequest.class));
    }

    @Test
    void testUpdateBlogStatus_Exception() {
        // Mock a general exception
        doThrow(new RuntimeException("Internal error")).when(blogFeign).updateBlogStatus(any(BlogStatusUpdateRequest.class));
        // Call the service method
        BlogStatusUpdateRequest request = new BlogStatusUpdateRequest(123L, "APPROVED");
        ApiResponse<String> response = blogService.updateBlogStatus(request);
        // Verify the result
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals("Failed to update blog status.", response.getMessage());
        // Verify the interaction with the Feign client
        verify(blogFeign, times(1)).updateBlogStatus(any(BlogStatusUpdateRequest.class));
    }

    @Test
    void testGetFilteredBlogs_WithBlogs() {
        // Mock Feign client response
        when(blogFeign.getFilteredBlogs(anyString(), anyString(), anyString()))
                .thenReturn(ResponseEntity.ok(blogResponse));
        // Call the service method
        ApiResponse<BlogResponse> response = blogService.getFilteredBlogs("1", "2", "3");
        // Verify the result
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("All blogs fetched successfully!", response.getMessage());
        // Verify the interaction with the Feign client
        verify(blogFeign, times(1)).getFilteredBlogs(anyString(), anyString(), anyString());
    }

    @Test
    void testGetFilteredBlogs_NoBlogs() {
        // Mock Feign client response with null data
        ApiResponse<BlogResponse> emptyResponse = ApiResponse.<BlogResponse>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("No blogs found.")
                .data(null)
                .build();
        when(blogFeign.getFilteredBlogs(anyString(), anyString(), anyString()))
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).body(emptyResponse));
        // Call the service method
        ApiResponse<BlogResponse> response = blogService.getFilteredBlogs("1", "2", "3");
        // Verify the result
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("No blogs found.", response.getMessage());
        assertNull(response.getData());
        // Verify the interaction with the Feign client
        verify(blogFeign, times(1)).getFilteredBlogs(anyString(), anyString(), anyString());
    }
}
