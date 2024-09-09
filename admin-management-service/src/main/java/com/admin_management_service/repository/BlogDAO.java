package com.admin_management_service.repository;

import com.admin_management_service.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogDAO extends JpaRepository<Blog,Long> {
    Page<Blog> findAll(Pageable pageable);
}
