package com.admin_management_service.service;

import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.CategoryDTO;
import com.admin_management_service.dto.CategoryStatusDTO;
import com.admin_management_service.entity.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> getall();
    public String add(CategoryDTO categoryDTO, String Token);
    public String update(CategoryDTO categoryDTO,String Token);
    public ApiResponse<Object> setCategoryStatus(CategoryStatusDTO categoryStatusDTO, String token);

}
