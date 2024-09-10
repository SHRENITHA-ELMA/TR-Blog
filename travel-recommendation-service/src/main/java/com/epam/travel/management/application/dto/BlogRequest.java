package com.epam.travel.management.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class BlogRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Content is mandatory")
    @Size(min = 20, message = "Content must be at least 20 characters long")
    private String content;

    private List<String> images;

    @NotBlank(message = "City is mandatory")
    @Size(max = 50, message = "City must be less than or equal to 50 characters")
    private String city;

    @NotBlank(message = "Country is mandatory")
    @Size(max = 50, message = "Country must be less than or equal to 50 characters")
    private String country;
}
