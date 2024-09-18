package com.admin_management_service.repository;

import com.admin_management_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryDAO extends JpaRepository<Category,String> {
    Category findByCategoryNameIgnoreCase(String categoryName);
}
