package com.epam.travel.management.application.serviceImpl;

import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogRequest;
import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.entity.UserResponse;
import com.epam.travel.management.application.exceptions.UnauthorizedAccessException;
import com.epam.travel.management.application.exceptions.UserNotFoundException;
import com.epam.travel.management.application.feign.UserClient;
import com.epam.travel.management.application.repository.BlogRepository;
import com.epam.travel.management.application.service.BlogService;
import com.epam.travel.management.application.serviceImpl.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

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
}
