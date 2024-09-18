package com.epam.travel.management.application.service;

import com.epam.travel.management.application.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

public interface BlogService {

    ApiResponse<Object> createBlog(HttpServletRequest request, BlogRequest blogRequest);

    void updateBlogStatus(Long blogId, String status);

    ApiResponse<BlogResponse> getAllBlogs();

    ApiResponse<BlogResponse> getFilteredBlogs(String categoryId,String regionId,String countryId);

}
