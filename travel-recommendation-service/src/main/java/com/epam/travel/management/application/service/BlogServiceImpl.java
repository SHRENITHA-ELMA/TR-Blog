package com.epam.travel.management.application.service;

import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

}
