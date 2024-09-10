package com.epam.user.management.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationResponse {
    private String status,message,error,data;
}
