package com.epam.travel.management.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class BlogRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Content is mandatory")
    @Size(min = 20, message = "Content must be at least 20 characters long")
    private String content;

    private MultipartFile image;

    @NotBlank(message = "RegionId is mandatory")
    private String regionId;

    @NotBlank(message = "CategoryId is mandatory")
    private String categoryId;

    @NotBlank(message = "CountryId is mandatory")
    private String countryId;
}
