package com.admin_management_service.controller;
import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.CategoryDTO;
import com.admin_management_service.dto.CategoryStatusDTO;
import com.admin_management_service.entity.Category;
import com.admin_management_service.service.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
class CategoryControllerTest {
    @InjectMocks
    private CategoryController categoryController;
    @Mock
    private CategoryServiceImpl categoryServiceImpl;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetAll() {
        List<Category> categories = List.of(new Category());
        when(categoryServiceImpl.getall()).thenReturn(categories);
        ResponseEntity<ApiResponse<List<Category>>> response = categoryController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Categories fetched successfully", response.getBody().getMessage());
        assertEquals(categories, response.getBody().getData());
    }
    @Test
    void testCreate() {
        CategoryDTO categoryDTO = new CategoryDTO();
        String token = "valid-token";
        when(categoryServiceImpl.add(any(CategoryDTO.class), eq(token))).thenReturn(null);
        ResponseEntity<ApiResponse> response = categoryController.create(categoryDTO, token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category added successfully", response.getBody().getMessage());
    }
    @Test
    void testUpdateCategory() {
        CategoryDTO categoryDTO = new CategoryDTO();
        String token = "valid-token";
        when(categoryServiceImpl.update(any(CategoryDTO.class), eq(token))).thenReturn(null);
        ResponseEntity<ApiResponse> response = categoryController.updateCategory(categoryDTO, token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category updated successfully", response.getBody().getMessage());
    }
    @Test
    void testSetCategoryStatus() {
        CategoryStatusDTO categoryStatusDTO = new CategoryStatusDTO();
        String token = "valid-token";
        ApiResponse<Object> mockResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Status updated")
                .build();
        when(categoryServiceImpl.setCategoryStatus(any(CategoryStatusDTO.class), eq(token))).thenReturn(mockResponse);
        ResponseEntity<ApiResponse<Object>> response = categoryController.setCategoryStatus(categoryStatusDTO, token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated", response.getBody().getMessage());
    }
}
