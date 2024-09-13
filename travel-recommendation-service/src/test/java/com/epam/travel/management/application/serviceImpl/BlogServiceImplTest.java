package com.epam.travel.management.application.serviceImpl;

import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogRequest;
import com.epam.travel.management.application.dto.ViewBlogResponse;
import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.entity.UserResponse;
import com.epam.travel.management.application.exceptions.UnauthorizedAccessException;
import com.epam.travel.management.application.exceptions.UserNotFoundException;
import com.epam.travel.management.application.feign.UserClient;
import com.epam.travel.management.application.repository.BlogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private BlogServiceImpl blogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBlog_Success() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        BlogRequest blogRequest = BlogRequest.builder()
                .title("Test Title")
                .content("Test Content")
                .image(null)
                .regionId("1")
                .countryId("1")
                .categoryId("1")
                .build();

        UserResponse userResponse = new UserResponse(); // Set up user details

        ResponseEntity<UserResponse> userResponseEntity = new ResponseEntity<>(userResponse, HttpStatus.OK);
        when(userClient.getUserFromToken("token")).thenReturn(userResponseEntity);

        // Act
        ApiResponse<Object> response = blogService.createBlog(request, blogRequest);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("Blog has been created successfully", response.getMessage());
        verify(blogRepository, times(1)).save(any(Blog.class));
    }

    @Test
    void createBlog_Unauthorized_MissingHeader() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        BlogRequest blogRequest = BlogRequest.builder()
                .title("Test Title")
                .content("Test Content")
                .image(null)
                .regionId("1")
                .countryId("1")
                .categoryId("1")
                .build();

        // Act & Assert
        assertThrows(UnauthorizedAccessException.class, () -> blogService.createBlog(request, blogRequest));
    }

    @Test
    void createBlog_Unauthorized_InvalidUser() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        BlogRequest blogRequest = BlogRequest.builder()
                .title("Test Title")
                .content("Test Content")
                .image(null)
                .regionId("1")
                .countryId("1")
                .categoryId("1")
                .build();

        ResponseEntity<UserResponse> userResponseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        when(userClient.getUserFromToken("token")).thenReturn(userResponseEntity);

        // Act & Assert
        assertThrows(UnauthorizedAccessException.class, () -> blogService.createBlog(request, blogRequest));
    }

    @Test
    void createBlog_UserNotFound() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        BlogRequest blogRequest = BlogRequest.builder()
                .title("Test Title")
                .content("Test Content")
                .image(null)
                .regionId("1")
                .countryId("1")
                .categoryId("1")
                .build();

        ResponseEntity<UserResponse> userResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(userClient.getUserFromToken("token")).thenReturn(userResponseEntity);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> blogService.createBlog(request, blogRequest));
    }
    @Test
    void getApprovedBlogs_Success() {
        List<Blog> approvedBlogs = new ArrayList<>();
        Blog blog = Blog.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .userName("Test User")
                .userProfileImage("Test Image")
                .status("APPROVED")
                .imageUrl("http://example.com/image")
                .createdAt(new Date())
                .countryId("1")
                .regionId("1")
                .categoryId("1")
                .build();
        approvedBlogs.add(blog);

        when(blogRepository.findByStatus("APPROVED")).thenReturn(approvedBlogs);

        ApiResponse<List<ViewBlogResponse>> response = blogService.getApprovedBlogs();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Blogs retrieved successfully.", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals("Test Title", response.getData().get(0).getTitle());
        verify(blogRepository, times(1)).findByStatus("APPROVED");
    }

    @Test
    void getApprovedBlogs_NoBlogsFound() {
        List<Blog> emptyBlogs = new ArrayList<>();
        when(blogRepository.findByStatus("APPROVED")).thenReturn(emptyBlogs);

        ApiResponse<List<ViewBlogResponse>> response = blogService.getApprovedBlogs();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("No Blogs found.", response.getMessage());
        assertEquals(0, response.getData().size());
        verify(blogRepository, times(1)).findByStatus("APPROVED");
    }
}
