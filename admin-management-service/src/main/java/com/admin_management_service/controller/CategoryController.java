package com.admin_management_service.controller;

import com.admin_management_service.dto.*;
import com.admin_management_service.entity.Category;
import com.admin_management_service.service.CategoryServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping("categories")
    public ResponseEntity<ApiResponse<List<Category>>> getAll() {
        List<Category> categories = categoryServiceImpl.getall();

        ApiResponse<List<Category>> response = ApiResponse.<List<Category>>builder()
                .status(HttpStatus.OK.value())
                .message("Categories fetched successfully")
                .data(categories)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("categories")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CategoryDTO categoryDTO, @RequestHeader("Token") String token) {
        categoryServiceImpl.add(categoryDTO, token);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Category added successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("categories")
    public ResponseEntity<ApiResponse<Void>> update(@Valid @RequestBody CategoryDTO categoryDTO, @RequestHeader("Token") String token) {
        categoryServiceImpl.update(categoryDTO, token);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Category updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/categoryStatus")
    public ResponseEntity<ApiResponse<Object>> setCategoryStatus(@RequestBody CategoryStatusDTO categoryStatusDTO, @RequestHeader("Token") String token) {
        ApiResponse<Object> response = categoryServiceImpl.setCategoryStatus(categoryStatusDTO, token);
        return ResponseEntity.ok(response);
    }

}
