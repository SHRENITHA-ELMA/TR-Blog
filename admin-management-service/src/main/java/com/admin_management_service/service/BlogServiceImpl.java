package com.admin_management_service.service;

import com.admin_management_service.entity.Blog;
import com.admin_management_service.repository.BlogDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService{
    private final BlogDAO blogDAO;
    @Override
    public Page<Blog> getAllBlogs(Pageable pageable) {
        return blogDAO.findAll(pageable);
    }
    @Override
    public void enableOrDisableBlog(Long id, boolean enabled) {
        Optional<Blog> blogOptional = blogDAO.findById(id);
        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();
            blog.setEnabled(enabled);
            blogDAO.save(blog);
        }
    }

}
