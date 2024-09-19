package com.epam.travel.management.application.feign;

import com.admin_management_service.dto.FilterDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
@FeignClient(name = "admin-management-service")
public interface AdminBlogClient {

    @GetMapping("/adminBlog/filterData")
    ResponseEntity<FilterDataResponse> getFilterData();
}
