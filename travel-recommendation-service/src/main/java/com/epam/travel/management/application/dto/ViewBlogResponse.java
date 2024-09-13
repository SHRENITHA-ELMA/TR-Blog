package com.epam.travel.management.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewBlogResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String userName;
    private String userProfileImage;
    private String regionId;
    private String categoryId;
    private String countryId;
    private Date createdAt;
    private String status;
}
