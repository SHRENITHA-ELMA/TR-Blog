package com.admin_management_service.dto;

import jakarta.persistence.Column;

import java.util.Date;

public class BlogResponse {

    private Long blogId;
    private String title;
    private String content;
    private String userName;
    private Date updatedAt;
    private String city;
    private String country;
}
