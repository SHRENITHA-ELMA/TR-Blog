package com.admin_management_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegionStatusDTO {
    @NotBlank(message = "Category Code is mandatory")
    private String regionId;

    @NotNull(message = "Enable field is required")
    private Boolean enabled;
}
