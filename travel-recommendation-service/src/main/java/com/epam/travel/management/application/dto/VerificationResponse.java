package com.epam.travel.management.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationResponse {
    private int status;
    private String message;
}
