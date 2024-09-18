package com.admin_management_service.dto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class CategoryDTO {
    @NotBlank(message = "Category Code is mandatory")
    private String categoryId;
    @NotBlank(message = "Category is must")
   // @Size(min=1,message = "Size of the Category should be more than one")
    private String categoryName;
   // @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;
}
