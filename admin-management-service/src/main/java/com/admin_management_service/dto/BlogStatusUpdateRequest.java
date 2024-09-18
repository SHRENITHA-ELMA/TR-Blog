package com.admin_management_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogStatusUpdateRequest {
    private Long id;
    private String status;
}

