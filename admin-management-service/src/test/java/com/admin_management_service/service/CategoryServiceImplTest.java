package com.admin_management_service.service;

import com.admin_management_service.constant.Messages;
import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.CategoryDTO;
import com.admin_management_service.dto.CategoryStatusDTO;
import com.admin_management_service.entity.Category;
import com.admin_management_service.exceptions.*;
import com.admin_management_service.repository.CategoryDAO;
import com.admin_management_service.utility.VerificationUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryDAO categoryDAO;

    @Mock
    private VerificationUtility verificationUtility;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getall method
    @Test
    void testGetAll_Success() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("1", "Category1", true));
        when(categoryDAO.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getall();
        assertEquals(1, result.size());
        assertEquals("Category1", result.get(0).getCategoryName());
    }

    @Test
    void testGetAll_ThrowsNullValueException() {
        when(categoryDAO.findAll()).thenReturn(new ArrayList<>());

        NullValueException exception = assertThrows(NullValueException.class, () -> {
            categoryService.getall();
        });

        assertEquals("Add categories before Fetching", exception.getMessage());
    }

    // Test for add method
    @Test
    void testAdd_Success() {
        CategoryDTO categoryDTO = new CategoryDTO("1", "NewCategory", true); // Updated constructor
        when(categoryDAO.findById(categoryDTO.getCategoryId())).thenReturn(Optional.empty());
        when(categoryDAO.findByCategoryNameIgnoreCase(categoryDTO.getCategoryName())).thenReturn(null);
        when(verificationUtility.isValid(anyString())).thenReturn(true);

        String result = categoryService.add(categoryDTO, "validToken");
        assertEquals("Category added Successfully", result);
        verify(categoryDAO, times(1)).save(any(Category.class));
    }

    @Test
    void testAdd_ThrowsValueExistsException_WhenIdExists() {
        CategoryDTO categoryDTO = new CategoryDTO("1", "NewCategory", true); // Updated constructor
        when(categoryDAO.findById(categoryDTO.getCategoryId())).thenReturn(Optional.of(new Category()));

        ValueExistsException exception = assertThrows(ValueExistsException.class, () -> {
            categoryService.add(categoryDTO, "validToken");
        });

        assertEquals("Try with another ID", exception.getMessage());
    }

    @Test
    void testAdd_ThrowsValueExistsException_WhenCategoryExists() {
        CategoryDTO categoryDTO = new CategoryDTO("1", "ExistingCategory", true); // Updated constructor
        when(categoryDAO.findById(categoryDTO.getCategoryId())).thenReturn(Optional.empty());
        when(categoryDAO.findByCategoryNameIgnoreCase(categoryDTO.getCategoryName())).thenReturn(new Category());

        ValueExistsException exception = assertThrows(ValueExistsException.class, () -> {
            categoryService.add(categoryDTO, "validToken");
        });

        assertEquals("Category already existed", exception.getMessage());
    }

    @Test
    void testAdd_ThrowsVerificationException() {
        CategoryDTO categoryDTO = new CategoryDTO("1", "NewCategory", true); // Updated constructor
        when(categoryDAO.findById(categoryDTO.getCategoryId())).thenReturn(Optional.empty());
        when(categoryDAO.findByCategoryNameIgnoreCase(categoryDTO.getCategoryName())).thenReturn(null);
        when(verificationUtility.isValid(anyString())).thenReturn(false);

        Verfication exception = assertThrows(Verfication.class, () -> {
            categoryService.add(categoryDTO, "invalidToken");
        });

        assertEquals(Messages.verficationMessage, exception.getMessage());
    }

    // Test for update method
    @Test
    void testUpdate_Success() {
        CategoryDTO categoryDTO = new CategoryDTO("1", "UpdatedCategory", true); // Updated constructor
        Category category = new Category("1", "OldCategory", true);
        when(categoryDAO.findById(categoryDTO.getCategoryId())).thenReturn(Optional.of(category));
        when(verificationUtility.isValid(anyString())).thenReturn(true);

        String result = categoryService.update(categoryDTO, "validToken");
        assertEquals("Category Updated Successfully", result);
        verify(categoryDAO, times(1)).save(category);
    }

    @Test
    void testUpdate_ThrowsValueNotFoundException() {
        CategoryDTO categoryDTO = new CategoryDTO("1", "UpdatedCategory", true); // Updated constructor
        when(categoryDAO.findById(categoryDTO.getCategoryId())).thenReturn(Optional.empty());

        ValueNotFoundException exception = assertThrows(ValueNotFoundException.class, () -> {
            categoryService.update(categoryDTO, "validToken");
        });

        assertEquals("Categories Not Found", exception.getMessage());
    }

    @Test
    void testUpdate_ThrowsVerificationException() {
        CategoryDTO categoryDTO = new CategoryDTO("1", "UpdatedCategory", true); // Updated constructor
        when(categoryDAO.findById(categoryDTO.getCategoryId())).thenReturn(Optional.of(new Category()));
        when(verificationUtility.isValid(anyString())).thenReturn(false);

        Verfication exception = assertThrows(Verfication.class, () -> {
            categoryService.update(categoryDTO, "invalidToken");
        });

        assertEquals("Admin verification failed.", exception.getMessage());
    }

    // Test for setCategoryStatus method
    @Test
    void testSetCategoryStatus_Success_Enable() {
        CategoryStatusDTO categoryStatusDTO = new CategoryStatusDTO("1", true);
        Category category = new Category("1", "Category", false);
        when(categoryDAO.findById(categoryStatusDTO.getCategoryId())).thenReturn(Optional.of(category));
        when(verificationUtility.isValid(anyString())).thenReturn(true);

        ApiResponse<Object> result = categoryService.setCategoryStatus(categoryStatusDTO, "validToken");
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertEquals("Category enabled successfully", result.getMessage());
        verify(categoryDAO, times(1)).save(category);
    }

    @Test
    void testSetCategoryStatus_Success_Disable() {
        CategoryStatusDTO categoryStatusDTO = new CategoryStatusDTO("1", false);
        Category category = new Category("1", "Category", true);
        when(categoryDAO.findById(categoryStatusDTO.getCategoryId())).thenReturn(Optional.of(category));
        when(verificationUtility.isValid(anyString())).thenReturn(true);

        ApiResponse<Object> result = categoryService.setCategoryStatus(categoryStatusDTO, "validToken");
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertEquals("Category disabled successfully", result.getMessage());
        verify(categoryDAO, times(1)).save(category);
    }

    @Test
    void testSetCategoryStatus_ThrowsVerificationException() {
        CategoryStatusDTO categoryStatusDTO = new CategoryStatusDTO("1", true);
        when(verificationUtility.isValid(anyString())).thenReturn(false);

        Verfication exception = assertThrows(Verfication.class, () -> {
            categoryService.setCategoryStatus(categoryStatusDTO, "invalidToken");
        });

        assertEquals("Admin verification failed.", exception.getMessage());
    }

    @Test
    void testSetCategoryStatus_ThrowsValueNotFoundException() {
        CategoryStatusDTO categoryStatusDTO = new CategoryStatusDTO("1", true);
        when(verificationUtility.isValid(anyString())).thenReturn(true);
        when(categoryDAO.findById(categoryStatusDTO.getCategoryId())).thenReturn(Optional.empty());

        ValueNotFoundException exception = assertThrows(ValueNotFoundException.class, () -> {
            categoryService.setCategoryStatus(categoryStatusDTO, "validToken");
        });

        assertEquals("Category not found for ID", exception.getMessage());
    }
}
