package com.admin_management_service.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegionDTO {
    @NotBlank(message = "Category Code is mandatory")
    private String regionId;
    @NotBlank(message = "Category is must")
    // @Size(min=1,message = "Size of the Category should be more than one")
    private String regionName;
    // @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;
}
