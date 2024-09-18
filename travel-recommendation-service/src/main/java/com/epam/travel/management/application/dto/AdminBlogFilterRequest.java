package com.epam.travel.management.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminBlogFilterRequest {
    private String categoryId;
    private String regionId;
    private String countryId;
}
