package com.admin_management_service.controller;

import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.RegionDTO;
import com.admin_management_service.dto.RegionStatusDTO;
import com.admin_management_service.entity.Region;
import com.admin_management_service.service.RegionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RegionControllerTest {
    @Mock
    private RegionServiceImpl regionServiceImpl;
    @InjectMocks
    private RegionController regionController;
    @Test
    void testGetAllRegions() {
        List<Region> regions = Collections.singletonList(new Region());
        when(regionServiceImpl.getall()).thenReturn(regions);
        ResponseEntity<ApiResponse<List<Region>>> response = regionController.getAll();
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Regions fetched successfully", response.getBody().getMessage());
        assertEquals(regions, response.getBody().getData());
        verify(regionServiceImpl, times(1)).getall();
    }
    @Test
    void testCreateRegion() {
        RegionDTO regionDTO = new RegionDTO();
        String token = "dummyToken";
        ResponseEntity<ApiResponse> response = regionController.create(regionDTO, token);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Region added successfully", response.getBody().getMessage());
        verify(regionServiceImpl, times(1)).add(regionDTO, token);
    }
    @Test
    void testUpdateRegion() {
        RegionDTO regionDTO = new RegionDTO();
        String token = "dummyToken";
        ResponseEntity<ApiResponse> response = regionController.update(regionDTO, token);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Region updated successfully", response.getBody().getMessage());
        verify(regionServiceImpl, times(1)).update(regionDTO, token);
    }
    @Test
    void testSetRegionStatus() {
        RegionStatusDTO regionStatusDTO = new RegionStatusDTO();
        String token = "dummyToken";
        ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("Status updated")
                .build();
        when(regionServiceImpl.setRegionStatus(regionStatusDTO, token)).thenReturn(apiResponse);
        ResponseEntity<ApiResponse<Object>> response = regionController.setRegionStatus(regionStatusDTO, token);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Status updated", response.getBody().getMessage());
        verify(regionServiceImpl, times(1)).setRegionStatus(regionStatusDTO, token);
    }
}
