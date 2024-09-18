package com.admin_management_service.service;
import com.admin_management_service.dto.*;
import com.admin_management_service.entity.Region;
import java.util.List;
public interface RegionService {
    public List<Region> getall();
    public String add(RegionDTO regionDTO, String Token);
    public String update(RegionDTO regionDTO,String Token);
    public ApiResponse<Object> setRegionStatus(RegionStatusDTO regionStatusDTO, String token);
}
