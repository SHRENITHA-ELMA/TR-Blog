package com.epam.travel.management.application.serviceImpl;

import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogRequest;
import com.epam.travel.management.application.dto.ViewBlogRequest;
import com.epam.travel.management.application.dto.ViewBlogResponse;
import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.entity.UserResponse;
import com.epam.travel.management.application.exceptions.UnauthorizedAccessException;
import com.epam.travel.management.application.exceptions.UserNotFoundException;
import com.epam.travel.management.application.feign.UserClient;
import com.epam.travel.management.application.repository.BlogRepository;
import com.epam.travel.management.application.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserClient userClient;
    private final FileStorageService fileStorageService;

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

    public ApiResponse<List<ViewBlogResponse>> getApprovedBlogs() {
        try {
            List<Blog> approvedBlogs = blogRepository.findByStatus("accept");
            if (approvedBlogs.isEmpty()) {
                return ApiResponse.<List<ViewBlogResponse>>builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("No Blogs found.")
                        .data(null)
                        .build();
            }
            List<ViewBlogResponse> viewBlogs = approvedBlogs.stream()
                    .map(blog -> ViewBlogResponse.builder()
                            .id(blog.getId())
                            .content(blog.getContent())
                            .title(blog.getTitle())
                            .userName(blog.getUserName())
                            .userProfileImage(blog.getUserProfileImage())
                            .status(blog.getStatus())
                            .imageUrl(blog.getImageUrl())
                            .createdAt(blog.getCreatedAt())
                            .countryId(blog.getCountryId())
                            .categoryId(blog.getCategoryId())
                            .regionId(blog.getRegionId())
                            .build()
                    ).collect(Collectors.toList());
            return ApiResponse.<List<ViewBlogResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Blogs successfully retrieved")
                    .data(viewBlogs)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<ViewBlogResponse>>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("An error occured.")
                    .data(null)
                    .build();
        }
    }
    @Override
    public ApiResponse<List<ViewBlogResponse>> getApprovedBlogsByFilters(ViewBlogRequest filterRequest) {
        try{
            List<Blog> blogs = blogRepository.findByStatusAndFilters(
                    filterRequest.getRegionId(),
                    filterRequest.getCategoryId(),
                    filterRequest.getCountryId()
            );
            if (blogs.isEmpty()) {
                return ApiResponse.<List<ViewBlogResponse>>builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("No blogs found for the given filters.")
                        .data(null)
                        .build();
            }

            // Map Blog entities to ViewBlogResponse DTOs
            List<ViewBlogResponse> viewBlogs = blogs.stream()
                    .map(blog -> ViewBlogResponse.builder()
                            .id(blog.getId())
                            .title(blog.getTitle())
                            .content(blog.getContent())
                            .imageUrl(blog.getImageUrl())
                            .status(blog.getStatus())
                            .createdAt(blog.getCreatedAt())
                            .userName(blog.getUserName())
                            .userProfileImage(blog.getUserProfileImage())
                            .countryId(blog.getCountryId())
                            .regionId(blog.getRegionId())
                            .categoryId(blog.getCategoryId())
                            .build()
                    ).collect(Collectors.toList());

            return ApiResponse.<List<ViewBlogResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Blogs successfully retrieved.")
                    .data(viewBlogs)
                    .build();
        }
        catch(Exception e){
            return ApiResponse.<List<ViewBlogResponse>>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("An error occurred while retrieving the blogs.")
                    .data(null)
                    .build();
        }
    }
}
