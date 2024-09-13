package com.admin_management_service.service;

import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.BlogResponse;
import com.admin_management_service.dto.BlogStatusUpdateRequest;
import com.admin_management_service.exceptions.InvalidStatusException;
import com.admin_management_service.feign.BlogFeign;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log
public class BlogService {

    private final BlogFeign blogFeign;

        public ApiResponse<BlogResponse>getAllBlogs(HttpServletRequest request)
        {

                log.info("called service");
                // Call Feign client to get blogs from travel-management-service
                ResponseEntity<ApiResponse<BlogResponse>> blogResponse = blogFeign.getAllBlogs();
                log.info("ERROR WITH BLOGRESPONSE");

                if (blogResponse.getBody().getData()==null) {
                    return ApiResponse.<BlogResponse>builder()
                            .status(HttpStatus.NO_CONTENT.value())
                            .message("No blogs found.")
                            .data(null)
                            .build();
                }

                return ApiResponse.<BlogResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("All blogs fetched successfully!")
                        .data(blogResponse.getBody().getData())
                        .build();
        }

        public ApiResponse<String> updateBlogStatus(BlogStatusUpdateRequest request) {
            try {
                blogFeign.updateBlogStatus(request);
                return ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Blog status updated successfully!")
                        .data(null)
                        .build();
            } catch (InvalidStatusException e) {
                return ApiResponse.<String>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .data(null)
                        .build();
            } catch (Exception e) {
                return ApiResponse.<String>builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Failed to update blog status.")
                        .data(null)
                        .build();
            }
        }
    }