package com.epam.user.management.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEnableDisableRequest {

    @NotNull(message = "Id field is required")
    private Long id;

    @NotNull(message = "Enable field is required")
    private Boolean enable;
}
