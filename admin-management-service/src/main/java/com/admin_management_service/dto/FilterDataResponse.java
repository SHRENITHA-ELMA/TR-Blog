package com.admin_management_service.dto;

import com.admin_management_service.entity.Category;
import com.admin_management_service.entity.Country;
import com.admin_management_service.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterDataResponse {
    private List<Country> countryData;
    private List<Region> regionData;
    private List<Category> categoryData;
}
