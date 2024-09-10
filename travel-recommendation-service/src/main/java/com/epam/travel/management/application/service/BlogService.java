package com.epam.travel.management.application.service;

import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface BlogService {

    ApiResponse<Object> createBlog(HttpServletRequest request, BlogRequest blogRequest);
}
