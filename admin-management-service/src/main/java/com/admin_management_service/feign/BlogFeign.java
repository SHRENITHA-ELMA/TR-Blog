package com.admin_management_service.feign;

import com.admin_management_service.dto.AdminBlogFilterRequest;
import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.BlogResponse;
import com.admin_management_service.dto.BlogStatusUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "travel-management-service")
public interface BlogFeign {

    @PutMapping("/blogs/status")
    void updateBlogStatus(@RequestBody BlogStatusUpdateRequest request);
    @GetMapping("/blogs/All")
    ResponseEntity<ApiResponse<BlogResponse>> getAllBlogs();
    @GetMapping("blogs/filter")
    public ResponseEntity<ApiResponse<BlogResponse>> getFilteredBlogs(@RequestParam("categoryId") String categoryId, @RequestParam("regionId")String regionId, @RequestParam("countryId")String countryId);

}


