package com.epam.travel.management.application.serviceImpl;

import com.admin_management_service.dto.FilterDataResponse;
import com.epam.travel.management.application.dto.*;
import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.entity.UserResponse;
import com.epam.travel.management.application.exceptions.ResourceNotFoundException;
import com.epam.travel.management.application.exceptions.UnauthorizedAccessException;
import com.epam.travel.management.application.exceptions.UserNotFoundException;
import com.epam.travel.management.application.exceptions.InvalidStatusException;
import com.epam.travel.management.application.feign.AdminBlogClient;
import com.epam.travel.management.application.feign.UserClient;
import com.epam.travel.management.application.repository.BlogRepository;
import com.epam.travel.management.application.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserClient userClient;
    private final FileStorageService fileStorageService;
    private final AdminBlogClient adminBlogClient;
    private final ObjectMapper objectMapper;

    @Override
    public ApiResponse<Object> createBlog(HttpServletRequest request, BlogRequest blogRequest) {
        if (request.getHeader("Authorization") == null) {
            throw new UnauthorizedAccessException("You aren't authorized");
        }
        String token = request.getHeader("Authorization").substring(7);
        ResponseEntity<UserResponse> userRes = userClient.getUserFromToken(token);
        if (userRes.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedAccessException("You are not authorized");
        } else if (userRes.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new UserNotFoundException("User not found");
        }

        String imageUrl=null;
        // Upload image to GitHub
        if (blogRequest.getImage() != null) {
            imageUrl = fileStorageService.uploadImageToGithub((MultipartFile) blogRequest.getImage());
        }

        UserResponse user = userRes.getBody();
        Blog blog = Blog.builder()
                .title(blogRequest.getTitle())
                .content(blogRequest.getContent())
                .imageUrl(imageUrl)
                .regionId(blogRequest.getRegionId())
                .countryId(blogRequest.getCountryId())
                .categoryId(blogRequest.getCategoryId())
                .createdAt(new Date())
                .status("Pending")
                .userName(user.getFirstName() + " " + user.getLastName())
                .userProfileImage(user.getImageUrl())
                .build();

        blogRepository.save(blog);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Blog has been created successfully")
                .build();
    }

    // You can also use the delete method if required
    public void deleteBlogImage(String fileName) {
        fileStorageService.deleteImageFromGithub(fileName);
    }
    public void updateBlogStatus(Long id, String status) {
        if (!"accept".equals(status) && !"reject".equals(status)) {
            throw new InvalidStatusException("Invalid status. Only 'accept' or 'reject' are allowed.");
        }
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        blog.setStatus(status);
        blogRepository.save(blog);
    }

    @Override
    public ApiResponse<BlogResponse> getAllBlogs() {
        try {
            List<Blog> blog = blogRepository.findAll();
            if (blog.isEmpty()) {
                return ApiResponse.<BlogResponse>builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message("No blogs found.")
                        .data(null) // You might want to return an empty BlogResponse here instead
                        .build();
            }
            BlogResponse blogResponse = BlogResponse.builder().blogs(blog).build();
            return ApiResponse.<BlogResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("All blogs fetched successfully")
                    .data(blogResponse)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.<BlogResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred while fetching blogs.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public ApiResponse<BlogResponse> getFilteredBlogs(String categoryId,String regionId,String countryId) {
            try {
                List<Blog> blog = blogRepository.getBlogsByCategoryIdOrRegionIdOrCountryId(categoryId, regionId, countryId);
                if (blog.isEmpty()) {
                    return ApiResponse.<BlogResponse>builder()
                            .status(HttpStatus.NO_CONTENT.value())
                            .message("No blogs found.")
                            .data(null) // You might want to return an empty BlogResponse here instead
                            .build();
                }
                BlogResponse blogResponse = BlogResponse.builder().blogs(blog).build();
                System.out.println(blogResponse);
                return ApiResponse.<BlogResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("All blogs fetched successfully")
                        .data(blogResponse)
                        .build();
            }
            catch(Exception e)
            {
                return ApiResponse.<BlogResponse>builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("An error occured while fetching data")
                        .data(null)
                        .build();
            }

    }
    @Override
    public ApiResponse<List<ViewBlogResponse>> getApprovedBlogs() {
        try {
            Optional<List<Blog>> optionalBlogs = Optional.ofNullable(blogRepository.findByStatus("accept"));
            if (optionalBlogs.isEmpty() || optionalBlogs.get().isEmpty()) {
                return ApiResponse.<List<ViewBlogResponse>>builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("No Blogs found.")
                        .data(null)
                        .build();
            }
            List<ViewBlogResponse> viewBlogs = optionalBlogs.get().stream()
                    .map(blog -> objectMapper.convertValue(blog, ViewBlogResponse.class))
                    .collect(Collectors.toList());
            return ApiResponse.<List<ViewBlogResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Blogs successfully retrieved")
                    .data(viewBlogs)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<ViewBlogResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred.")
                    .data(null)
                    .build();
        }
    }
    @Override
    public ApiResponse<List<ViewBlogResponse>> getApprovedFilters(ViewBlogRequest viewBlogRequest) {
        try {
            // Fetch blogs based on filters: regionId, categoryId, and countryId
            Optional<List<Blog>> optionalBlogs = Optional.ofNullable(
                    blogRepository.findByStatusAndFilters(
                            viewBlogRequest.getRegionId(),
                            viewBlogRequest.getCategoryId(),
                            viewBlogRequest.getCountryId()
                    )
            );

            // Check if blogs are present, otherwise return a NOT_FOUND response
            if (optionalBlogs.isEmpty() || optionalBlogs.get().isEmpty()) {
                return ApiResponse.<List<ViewBlogResponse>>builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("No blogs found for the given filters.")
                        .data(null)
                        .build();
            }

            // Fetch filter data from admin service
            ResponseEntity<FilterDataResponse> responseEntity;
            try {
                responseEntity = adminBlogClient.getFilterData();
            } catch (FeignException e) {
                // Handle Feign client error
                return ApiResponse.<List<ViewBlogResponse>>builder()
                        .status(HttpStatus.BAD_GATEWAY.value())
                        .message("Failed to retrieve filter data from the admin service.")
                        .data(null)
                        .build();
            }
            // Map Blog entities to ViewBlogResponse DTOs
            List<ViewBlogResponse> viewBlogs = optionalBlogs.get().stream()
                    .map(blog -> objectMapper.convertValue(blog, ViewBlogResponse.class))
                    .collect(Collectors.toList());

            // Return success response with the list of blogs
            return ApiResponse.<List<ViewBlogResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Blogs successfully retrieved.")
                    .data(viewBlogs)
                    .build();

        } catch (Exception e) {
            // Return error response in case of any exception
            return ApiResponse.<List<ViewBlogResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred while retrieving the blogs.")
                    .data(null)
                    .build();
        }
    }

}
