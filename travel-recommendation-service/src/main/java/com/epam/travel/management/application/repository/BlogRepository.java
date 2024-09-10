package com.epam.travel.management.application.repository;

import com.epam.travel.management.application.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
