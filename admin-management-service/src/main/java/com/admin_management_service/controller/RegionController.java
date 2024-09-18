package com.admin_management_service.controller;

import com.admin_management_service.dto.*;
import com.admin_management_service.entity.Region;
import com.admin_management_service.service.RegionServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("region")
@AllArgsConstructor
public class RegionController {
    private final RegionServiceImpl regionServiceImpl;
    @GetMapping("regions")
    public ResponseEntity<ApiResponse<List<Region>>> getAll() {
        List<Region> regions = regionServiceImpl.getall();

        ApiResponse<List<Region>> response = ApiResponse.<List<Region>>builder()
                .status(HttpStatus.OK.value())
                .message("Regions fetched successfully")
                .data(regions)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("regions")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody RegionDTO regionDTO, @RequestHeader("Token") String token) {
        regionServiceImpl.add(regionDTO, token);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Region added successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("regions")
    public ResponseEntity<ApiResponse<Void>> update(@Valid @RequestBody RegionDTO regionDTO, @RequestHeader("Token") String token) {
        regionServiceImpl.update(regionDTO, token);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Region updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/regionStatus")
    public ResponseEntity<ApiResponse<Object>> setRegionStatus(@RequestBody RegionStatusDTO regionStatusDTO, @RequestHeader("Token") String token) {
        ApiResponse<Object> response = regionServiceImpl.setRegionStatus(regionStatusDTO, token);
        return ResponseEntity.ok(response);
    }
}
