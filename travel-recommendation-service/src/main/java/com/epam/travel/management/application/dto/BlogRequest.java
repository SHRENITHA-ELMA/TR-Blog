package com.epam.travel.management.application.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class BlogRequest {

    private String title;

    private String content;

    private List<String> images;

    private String city;

    private String country;

}

