package com.epam.travel.management.application.service;

import com.epam.travel.management.application.dto.ApiResponse;
import com.epam.travel.management.application.dto.BlogRequest;
import com.epam.travel.management.application.dto.ViewBlogRequest;
import com.epam.travel.management.application.dto.ViewBlogResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BlogService {

    ApiResponse<Object> createBlog(HttpServletRequest request, BlogRequest blogRequest);
    ApiResponse<List<ViewBlogResponse>> getApprovedBlogs();
    ApiResponse<List<ViewBlogResponse>> getApprovedBlogsByFilters(ViewBlogRequest viewBlogRequest);
}
