//package com.epam.travel.management.application.serviceImpl;
//
//import com.epam.travel.management.application.dto.ApiResponse;
//import com.epam.travel.management.application.dto.BlogRequest;
//import com.epam.travel.management.application.entity.Blog;
//import com.epam.travel.management.application.entity.UserResponse;
//import com.epam.travel.management.application.exceptions.UnauthorizedAccessException;
//import com.epam.travel.management.application.exceptions.UserNotFoundException;
//import com.epam.travel.management.application.feign.UserClient;
//import com.epam.travel.management.application.repository.BlogRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class BlogServiceImplTest {
//
//    @Mock
//    private BlogRepository blogRepository;
//
//    @Mock
//    private UserClient userClient;
//
//    @InjectMocks
//    private BlogServiceImpl blogService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createBlog_Success() {
//        // Arrange
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getHeader("Authorization")).thenReturn("Bearer token");
//
//        BlogRequest blogRequest = BlogRequest.builder()
//                .title("Test Title")
//                .content("Test Content")
//                .image(null)
//                .regionId("1")
//                .countryId("1")
//                .categoryId("1")
//                .build();
//
//        UserResponse userResponse = new UserResponse(); // Set up user details
//
//        ResponseEntity<UserResponse> userResponseEntity = new ResponseEntity<>(userResponse, HttpStatus.OK);
//        when(userClient.getUserFromToken("token")).thenReturn(userResponseEntity);
//
//        // Act
//        ApiResponse<Object> response = blogService.createBlog(request, blogRequest);
//
//        // Assert
//        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
//        assertEquals("Blog has been created successfully", response.getMessage());
//        verify(blogRepository, times(1)).save(any(Blog.class));
//    }
//
//    @Test
//    void createBlog_Unauthorized_MissingHeader() {
//        // Arrange
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getHeader("Authorization")).thenReturn(null);
//
//        BlogRequest blogRequest = BlogRequest.builder()
//                .title("Test Title")
//                .content("Test Content")
//                .image(null)
//                .regionId("1")
//                .countryId("1")
//                .categoryId("1")
//                .build();
//
//        // Act & Assert
//        assertThrows(UnauthorizedAccessException.class, () -> blogService.createBlog(request, blogRequest));
//    }
//
//    @Test
//    void createBlog_Unauthorized_InvalidUser() {
//        // Arrange
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getHeader("Authorization")).thenReturn("Bearer token");
//
//        BlogRequest blogRequest = BlogRequest.builder()
//                .title("Test Title")
//                .content("Test Content")
//                .image(null)
//                .regionId("1")
//                .countryId("1")
//                .categoryId("1")
//                .build();
//
//        ResponseEntity<UserResponse> userResponseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        when(userClient.getUserFromToken("token")).thenReturn(userResponseEntity);
//
//        // Act & Assert
//        assertThrows(UnauthorizedAccessException.class, () -> blogService.createBlog(request, blogRequest));
//    }
//
//    @Test
//    void createBlog_UserNotFound() {
//        // Arrange
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getHeader("Authorization")).thenReturn("Bearer token");
//
//        BlogRequest blogRequest = BlogRequest.builder()
//                .title("Test Title")
//                .content("Test Content")
//                .image(null)
//                .regionId("1")
//                .countryId("1")
//                .categoryId("1")
//                .build();
//
//        ResponseEntity<UserResponse> userResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        when(userClient.getUserFromToken("token")).thenReturn(userResponseEntity);
//
//        // Act & Assert
//        assertThrows(UserNotFoundException.class, () -> blogService.createBlog(request, blogRequest));
//    }
//}
package com.epam.travel.management.application.serviceImpl;

import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogResponse;
import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.repository.BlogRepository;
import com.epam.travel.management.application.exceptions.InvalidStatusException;
import com.epam.travel.management.application.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogServiceImpl blogService;


    @Test
    void updateBlogStatus_Success() {
        // Arrange
        Long blogId = 1L;
        String newStatus = "accept";
        Blog blog = new Blog();
        blog.setId(blogId);
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        // No need to use doNothing() here since save() is not void

        // Act
        blogService.updateBlogStatus(blogId, newStatus);

        // Assert
        assertEquals(newStatus, blog.getStatus());
        verify(blogRepository).save(blog); // Verifies that save() was called with the correct argument
    }


    @Test
    void updateBlogStatus_InvalidStatus() {
        // Arrange
        Long blogId = 1L;
        String invalidStatus = "invalid";

        // Act & Assert
        Exception exception = assertThrows(InvalidStatusException.class, () -> {
            blogService.updateBlogStatus(blogId, invalidStatus);
        });

        assertEquals("Invalid status. Only 'accept' or 'reject' are allowed.", exception.getMessage());
    }

    @Test
    void updateBlogStatus_BlogNotFound() {
        // Arrange
        Long blogId = 1L;
        String status = "accept";
        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            blogService.updateBlogStatus(blogId, status);
        });

        assertEquals("Blog not found", exception.getMessage());
    }

    @Test
    void getAllBlogs_Success() {
        // Arrange
        List<Blog> blogs = Arrays.asList(new Blog(), new Blog());
        BlogResponse blogResponse = BlogResponse.builder().blogs(blogs).build();
        when(blogRepository.findAll()).thenReturn(blogs);

        // Act
        ApiResponse<BlogResponse> response = blogService.getAllBlogs();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("All blogs fetched successfully", response.getMessage());
        assertEquals(blogResponse, response.getData());
    }

    @Test
    void getAllBlogs_NoBlogsFound() {
        // Arrange
        when(blogRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ApiResponse<BlogResponse> response = blogService.getAllBlogs();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("No blogs found.", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void getAllBlogs_InternalServerError() {
        // Arrange
        when(blogRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ApiResponse<BlogResponse> response = blogService.getAllBlogs();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals("An error occurred while fetching blogs.", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void getFilteredBlogs_Success() {
        // Arrange
        String categoryId = "1";
        String regionId = "2";
        String countryId = "3";
        List<Blog> blogs = Arrays.asList(new Blog(), new Blog());
        BlogResponse blogResponse = BlogResponse.builder().blogs(blogs).build();
        when(blogRepository.getBlogsByCategoryIdOrRegionIdOrCountryId(categoryId, regionId, countryId)).thenReturn(blogs);

        // Act
        ApiResponse<BlogResponse> response = blogService.getFilteredBlogs(categoryId, regionId, countryId);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("All blogs fetched successfully", response.getMessage());
        assertEquals(blogResponse, response.getData());
    }

    @Test
    void getFilteredBlogs_NoBlogsFound() {
        // Arrange
        String categoryId = "1";
        String regionId = "2";
        String countryId = "3";
        when(blogRepository.getBlogsByCategoryIdOrRegionIdOrCountryId(categoryId, regionId, countryId)).thenReturn(Collections.emptyList());

        // Act
        ApiResponse<BlogResponse> response = blogService.getFilteredBlogs(categoryId, regionId, countryId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("No blogs found.", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void getFilteredBlogs_InternalServerError() {
        // Arrange
        String categoryId = "1";
        String regionId = "2";
        String countryId = "3";
        when(blogRepository.getBlogsByCategoryIdOrRegionIdOrCountryId(categoryId, regionId, countryId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ApiResponse<BlogResponse> response = blogService.getFilteredBlogs(categoryId, regionId, countryId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals("An error occured while fetching data", response.getMessage());
        assertNull(response.getData());
    }
}
