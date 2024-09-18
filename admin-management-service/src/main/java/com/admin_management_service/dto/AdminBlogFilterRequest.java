package com.admin_management_service.dto;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminBlogFilterRequest {
    private String categoryId;
    private String regionId;
    private String countryId;
}
