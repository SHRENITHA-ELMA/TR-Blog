package com.admin_management_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionStatusDTO {
    @NotBlank(message = "Category Code is mandatory")
    private String RegionId;

    @NotNull(message = "Enable field is required")
    private Boolean enabled;
}
