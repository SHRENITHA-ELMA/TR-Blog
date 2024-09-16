package com.epam.travel.management.application.dto;

import com.epam.travel.management.application.entity.Blog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {
    private List<Blog>blogs;
}
