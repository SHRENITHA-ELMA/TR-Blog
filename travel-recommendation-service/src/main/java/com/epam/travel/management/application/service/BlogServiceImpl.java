package com.epam.travel.management.application.service;

import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.entity.User;
import com.epam.travel.management.application.repository.BlogRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public Blog createBlog(String title, String content, List<String> images, String city, String country, Optional<User> user) {
        log.info(user.get().getFirstName()+" "+user.get().getLastName());
        Blog blog=Blog.builder()
                .createdAt(new Date())
                .updatedAt(new Date())
                .title(title)
                .content(content)
                .images(images)
                .city(city)
                .country(country)
                .isEnabled(true)
                .rating(2.5)
                .userName(user.get().getFirstName()+" "+user.get().getLastName())
                .userProfileImage(user.get().getImageUrl())
                .build();
        return blogRepository.save(blog);
    }

}
