package com.admin_management_service.dto;

import com.admin_management_service.entity.Country;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteResponse {
    private String status,message;
    private Country data;
}
