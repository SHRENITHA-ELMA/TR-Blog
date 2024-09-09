package com.admin_management_service.service;

import com.admin_management_service.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    public Page<Blog> getAllBlogs(Pageable pageable) ;
    public void enableOrDisableBlog(Long id, boolean enabled);

}
