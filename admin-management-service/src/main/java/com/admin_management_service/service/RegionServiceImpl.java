package com.admin_management_service.service;

import com.admin_management_service.constant.Messages;
import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.RegionDTO;
import com.admin_management_service.dto.RegionStatusDTO;
import com.admin_management_service.entity.Region;
import com.admin_management_service.exceptions.NullValueException;
import com.admin_management_service.exceptions.ValueExistsException;
import com.admin_management_service.exceptions.ValueNotFoundException;
import com.admin_management_service.exceptions.Verfication;
import com.admin_management_service.repository.RegionDAO;
import com.admin_management_service.utility.VerificationUtility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionDAO regionDAO;
    //private final ObjectMapper objectMapper;
    private VerificationUtility verificationUtility;
    //private final CountryFeign countryFeign;

    public List<Region> getall(){
        if(regionDAO.findAll().isEmpty()){
            throw new NullValueException("Add regions before Fetching");
        }
        return regionDAO.findAll();
    }
    public String add(RegionDTO regionDTO, String Token){
        if(regionDAO.findById(regionDTO.getRegionId()).isPresent()){
            throw new ValueExistsException("Region already present");
        }
        if (!verificationUtility.isValid(Token)) {
            throw new Verfication(Messages.verficationMessage);
        }
        Region region =Region.builder()
                .regionId(regionDTO.getRegionId())
                .regionName(regionDTO.getRegionName()).enabled(true).build();
        regionDAO.save(region);
        return "Region added Successfully";
    }

    public String update(RegionDTO regionDTO,String Token){
        Optional<Region> regionOpt = regionDAO.findById(regionDTO.getRegionId());
        if (regionOpt.isEmpty()) {
            throw new ValueNotFoundException("Region Not Found");
        }
        if (!verificationUtility.isValid(Token)) {
            throw new Verfication("Admin verification failed.");
        }
        Region region= regionOpt.get();
        region.setRegionName(regionDTO.getRegionName());
        regionDAO.save(region);
        return "Region Update Success";
    }

    public ApiResponse<Object> setRegionStatus(RegionStatusDTO regionStatusDTO, String token) {
        if (!verificationUtility.isValid(token)) {
            throw new Verfication("Admin verification failed.");
        }
        Region region = regionDAO.findById(regionStatusDTO.getRegionId())
                .orElseThrow(() -> new ValueNotFoundException("Region not found for ID")) ;
        region.setEnabled(regionStatusDTO.getEnabled());
        regionDAO.save(region);
        String action = Boolean.TRUE.equals(regionStatusDTO.getEnabled()) ? "enabled" : "disabled";
        ApiResponse<Object> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Region " + action + " successfully");
        return response;
    }

}
