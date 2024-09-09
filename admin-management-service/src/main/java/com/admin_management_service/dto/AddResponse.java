package com.admin_management_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddResponse {
    private String status,message,data;
}
