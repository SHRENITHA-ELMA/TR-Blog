package com.epam.travel.management.application.dto;

import com.epam.travel.management.application.entity.Blog;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
@Builder
public class BlogResponse {
    private List<Blog>blogs;
}
