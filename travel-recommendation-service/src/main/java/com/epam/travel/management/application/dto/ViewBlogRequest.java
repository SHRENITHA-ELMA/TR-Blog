package com.epam.travel.management.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewBlogRequest {

    private String regionId;
    private String categoryId;
    private String countryId;
}
