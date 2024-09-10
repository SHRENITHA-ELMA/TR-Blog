package com.epam.travel.management.application.serviceImpl;

import com.epam.travel.management.application.dto.BlogRequest;

import com.epam.travel.management.application.entity.Blog;
import com.epam.travel.management.application.entity.UserResponse;
import com.epam.travel.management.application.feign.UserClient;
import com.epam.travel.management.application.repository.BlogRepository;
import com.epam.travel.management.application.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    private final UserClient userClient;



    public void createBlog(HttpServletRequest request, BlogRequest blogRequest) {
        if(request.getHeader("Authorization")==null){

        }
        String token= request.getHeader("Authorization").substring(7);
        UserResponse user = userClient.getUserFromToken(token).getBody();

        Blog blog = Blog.builder()
                .title(blogRequest.getTitle())
                .content(blogRequest.getContent())
                .images(blogRequest.getImages())
                .city(blogRequest.getCity())
                .country(blogRequest.getCountry())
                .createdAt(new Date())
                .status("Pending")
                .rating(2.5)
                .userName(user.getFirstName()+" "+user.getLastName())
                .userProfileImage(user.getImageUrl())
                .build();

        blogRepository.save(blog);
    }
}
