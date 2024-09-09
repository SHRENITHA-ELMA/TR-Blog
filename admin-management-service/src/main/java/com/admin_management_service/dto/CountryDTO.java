package com.admin_management_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CountryDTO {
    @NotBlank(message = "Country Code is must")
    private String countryCode;

    @NotBlank(message = "Country is must")
    @Size(min=1,message = "Size of the Country should be more than one")
    private String country;

}
