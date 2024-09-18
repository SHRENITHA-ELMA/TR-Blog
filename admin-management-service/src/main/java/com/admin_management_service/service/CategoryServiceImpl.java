package com.admin_management_service.service;

import com.admin_management_service.constant.Messages;
import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.CategoryDTO;
import com.admin_management_service.dto.CategoryStatusDTO;
import com.admin_management_service.entity.Category;

import com.admin_management_service.exceptions.*;
import com.admin_management_service.repository.CategoryDAO;
import com.admin_management_service.utility.VerificationUtility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;
    //private final ObjectMapper objectMapper;
    private VerificationUtility verificationUtility;
    //private final CountryFeign countryFeign;

    public List<Category> getall(){
        if(categoryDAO.findAll().isEmpty()){
            throw new NullValueException("Add categories before Fetching");
        }
        return categoryDAO.findAll();
    }
//    public String add(CategoryDTO categoryDTO, String Token){
//        if(categoryDAO.findById(categoryDTO.getCategoryId()).isPresent()){
//            throw new ValueExistsException("Category already present");
//        }
//        if (!verificationUtility.isValid(Token)) {
//            throw new Verfication(Messages.verficationMessage);
//        }
//        Category category =Category.builder()
//                .categoryId(categoryDTO.getCategoryId())
//                .categoryName(categoryDTO.getCategoryName()).enabled(true).build();
//        categoryDAO.save(category);
//        return "Adding Success";
//    }
public String add(CategoryDTO categoryDTO, String token) {
    if (categoryDAO.findById(categoryDTO.getCategoryId()).isPresent()) {
        throw new ValueExistsException("Try with another ID");
    }
    Category existingCategory = categoryDAO.findByCategoryNameIgnoreCase(categoryDTO.getCategoryName());
    if (existingCategory!=null) {
        throw new ValueExistsException("Category already existed");
    }
    if (!verificationUtility.isValid(token)) {
        throw new Verfication(Messages.verficationMessage);
    }
    Category category = Category.builder()
            .categoryId(categoryDTO.getCategoryId())
            .categoryName(categoryDTO.getCategoryName())
            .enabled(true)
            .build();
    categoryDAO.save(category);

    return "Adding Success";
}

    public String update(CategoryDTO categoryDTO,String Token){
        Optional<Category> categoryOpt = categoryDAO.findById(categoryDTO.getCategoryId());
        if (categoryOpt.isEmpty()) {
            throw new ValueNotFoundException("Categories Not Found");
        }
        if (!verificationUtility.isValid(Token)) {
            throw new Verfication("Admin verification failed.");
        }
        Category category = categoryOpt.get();
        category.setCategoryName(categoryDTO.getCategoryName());
        categoryDAO.save(category);
        return "Category Update Success";
    }

public ApiResponse<Object> setCategoryStatus(CategoryStatusDTO categoryStatusDTO, String token) {
    if (!verificationUtility.isValid(token)) {
        throw new Verfication("Admin verification failed.");
    }
    Category category = categoryDAO.findById(categoryStatusDTO.getCategoryId())
            .orElseThrow(() -> new ValueNotFoundException("Category not found for ID")) ;
    category.setEnabled(categoryStatusDTO.getEnabled());
    categoryDAO.save(category);
    String action = Boolean.TRUE.equals(categoryStatusDTO.getEnabled()) ? "enabled" : "disabled";
    ApiResponse<Object> response = new ApiResponse<>();
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Category " + action + " successfully");
    return response;
}


}
