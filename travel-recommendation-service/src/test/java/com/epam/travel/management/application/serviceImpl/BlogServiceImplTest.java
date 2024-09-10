package com.epam.travel.management.application.serviceImpl;

import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogRequest;
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
}
