//package com.admin_management_service.controller;
//
//import com.admin_management_service.dto.AdminBlogRequest;
//import com.admin_management_service.dto.ApiResponse;
//import com.admin_management_service.feign.BlogFeign;
//import com.admin_management_service.service.BlogService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import jdk.jfr.Label;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.java.Log;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/blogs")
//@RequiredArgsConstructor
//@Log
//public class BlogController {
//    private final BlogService blogService;
//    private final BlogFeign blogFeign;
////    @GetMapping()
////    public Page<Blog> getAllBlogs(@RequestHeader("Authorization") String token,
////                                              @RequestParam(value = "page", defaultValue = "0") int page,
////    @RequestParam(value = "size", defaultValue = "10") int size)
////    {
////        return blogService.getAllBlogs(token , page , size);
////    }
//    @PutMapping("/status")
//    public ResponseEntity<ApiResponse<Object>> updateBlogStatus(
//            HttpServletRequest request,  @RequestBody AdminBlogRequest adminBlogRequest) {
//        log.info("Received AdminBlogRequest: {}");
//        // Call Feign client
//        try {
//            blogFeign.updateBlogStatus(adminBlogRequest);
//        } catch (Exception e) {
//            log.info("Feign client call failed logger");
//
//            throw e; // or handle as needed
//        }
//        ApiResponse<Object> response=blogService.updateBlogStatus(request,adminBlogRequest);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//}
package com.admin_management_service.controller;

import com.admin_management_service.dto.AdminBlogRequest;
import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.BlogResponse;
import com.admin_management_service.dto.BlogStatusUpdateRequest;
import com.admin_management_service.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    @GetMapping("/blogs/All")
    public ResponseEntity<ApiResponse<BlogResponse>> getAllBlogs(HttpServletRequest request){
        ApiResponse<BlogResponse> response=blogService.getAllBlogs(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/status")
    public ResponseEntity<ApiResponse<String>> updateBlogStatus(
            @RequestBody BlogStatusUpdateRequest request) {
        ApiResponse<String> response = blogService.updateBlogStatus(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
