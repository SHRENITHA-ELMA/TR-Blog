package com.admin_management_service.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CategoryDTO {
    @NotBlank(message = "Category Code is mandatory")
    private String categoryId;
    @NotBlank(message = "Category is mandatory")
    private String categoryName;
    private boolean enabled;
}
