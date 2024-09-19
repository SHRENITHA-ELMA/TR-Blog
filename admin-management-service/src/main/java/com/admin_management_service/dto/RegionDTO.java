package com.admin_management_service.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    @NotBlank(message = "Category Code is mandatory")
    private String regionId;
    @NotBlank(message = "Category is must")
    private String regionName;
    private boolean enabled;
}
