package com.admin_management_service.controllertest;

import com.admin_management_service.controller.BlogController;
import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.BlogResponse;
import com.admin_management_service.dto.BlogStatusUpdateRequest;
import com.admin_management_service.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
@RequiredArgsConstructor
public class BlogControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BlogService blogService;
    @Autowired
    private ObjectMapper objectMapper;
    private ApiResponse<BlogResponse> blogResponse;
    private ApiResponse<String> blogStatusResponse;

    @BeforeEach
    void setUp() {
        blogResponse = new ApiResponse<>(HttpStatus.OK.value(), "Success", new BlogResponse());
        blogStatusResponse = new ApiResponse<>(HttpStatus.OK.value(), "Blog status updated", "Success");
    }

    @Test
    void testGetAllBlogs() throws Exception {
        // Mock service response
        when(blogService.getAllBlogs()).thenReturn(blogResponse);
        // Perform GET request and verify response
        mockMvc.perform(get("/adminBlog/blogs/All")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetFilteredBlogs() throws Exception {
        // Mock service response
        when(blogService.getFilteredBlogs(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(blogResponse);
        // Perform GET request with filter parameters and verify response
        mockMvc.perform(get("/adminBlog/blogs/filter")
                .param("categoryId", "1")
                .param("regionId", "2")
                .param("countryId", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testUpdateBlogStatus() throws Exception {
        // Mock service response
        when(blogService.updateBlogStatus(any(BlogStatusUpdateRequest.class))).thenReturn(blogStatusResponse);
        // Create BlogStatusUpdateRequest object
        BlogStatusUpdateRequest request = new BlogStatusUpdateRequest(123L, "APPROVED");
        // Perform PUT request and verify response
        mockMvc.perform(put("/adminBlog/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Blog status updated"));
    }
}
