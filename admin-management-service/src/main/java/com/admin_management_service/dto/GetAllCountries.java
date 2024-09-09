package com.admin_management_service.dto;

import com.admin_management_service.entity.Country;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllCountries {
    private String status,message;
    private List<Country> data;
}
