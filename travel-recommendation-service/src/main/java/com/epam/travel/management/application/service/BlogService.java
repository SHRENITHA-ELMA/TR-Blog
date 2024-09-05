package com.epam.travel.management.application.service;

import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    List<Blog> getAllBlogs();

    Blog createBlog(String title, String content, List<String> images, String city, String country, Optional<User> user);
}
