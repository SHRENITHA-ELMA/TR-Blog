package com.admin_management_service.service;
import com.admin_management_service.constant.Messages;
import com.admin_management_service.dto.ApiResponse;
import com.admin_management_service.dto.RegionDTO;
import com.admin_management_service.dto.RegionStatusDTO;
import com.admin_management_service.entity.Region;
import com.admin_management_service.exceptions.*;
import com.admin_management_service.repository.RegionDAO;
import com.admin_management_service.utility.VerificationUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class RegionServiceImplTest {
    @Mock
    private RegionDAO regionDAO;
    @Mock
    private VerificationUtility verificationUtility;
    @InjectMocks
    private RegionServiceImpl regionService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetAll_Success() {
        List<Region> regions = new ArrayList<>();
        regions.add(new Region("1", "Region1", true));
        when(regionDAO.findAll()).thenReturn(regions);
        List<Region> result = regionService.getall();
        assertEquals(1, result.size());
        assertEquals("Region1", result.get(0).getRegionName());
    }
    @Test
    void testGetAll_ThrowsNullValueException() {
        when(regionDAO.findAll()).thenReturn(new ArrayList<>());
        NullValueException exception = assertThrows(NullValueException.class, () -> {
            regionService.getall();
        });
        assertEquals("Add regions before Fetching", exception.getMessage());
    }
    @Test
    void testAdd_Success() {
        RegionDTO regionDTO = new RegionDTO("1", "NewRegion", true);
        when(regionDAO.findById(regionDTO.getRegionId())).thenReturn(Optional.empty());
        when(verificationUtility.isValid(anyString())).thenReturn(true);
        String result = regionService.add(regionDTO, "validToken");
        assertEquals("Region added Successfully", result);
        verify(regionDAO, times(1)).save(any(Region.class));
    }
    @Test
    void testAdd_ThrowsValueExistsException_WhenIdExists() {
        RegionDTO regionDTO = new RegionDTO("1", "NewRegion", true);
        when(regionDAO.findById(regionDTO.getRegionId())).thenReturn(Optional.of(new Region()));
        ValueExistsException exception = assertThrows(ValueExistsException.class, () -> {
            regionService.add(regionDTO, "validToken");
        });
        assertEquals("Region already present", exception.getMessage());
    }
    @Test
    void testAdd_ThrowsVerificationException() {
        RegionDTO regionDTO = new RegionDTO("1", "NewRegion", true);
        when(regionDAO.findById(regionDTO.getRegionId())).thenReturn(Optional.empty());
        when(verificationUtility.isValid(anyString())).thenReturn(false);
        Verfication exception = assertThrows(Verfication.class, () -> {
            regionService.add(regionDTO, "invalidToken");
        });
        assertEquals(Messages.verficationMessage, exception.getMessage());
    }
    @Test
    void testUpdate_Success() {
        RegionDTO regionDTO = new RegionDTO("1", "UpdatedRegion", true);
        Region region = new Region("1", "OldRegion", true);
        when(regionDAO.findById(regionDTO.getRegionId())).thenReturn(Optional.of(region));
        when(verificationUtility.isValid(anyString())).thenReturn(true);
        String result = regionService.update(regionDTO, "validToken");
        assertEquals("Region Updated Successfully", result);
        verify(regionDAO, times(1)).save(region);
    }
    @Test
    void testUpdate_ThrowsValueNotFoundException() {
        RegionDTO regionDTO = new RegionDTO("1", "UpdatedRegion", true);
        when(regionDAO.findById(regionDTO.getRegionId())).thenReturn(Optional.empty());
        ValueNotFoundException exception = assertThrows(ValueNotFoundException.class, () -> {
            regionService.update(regionDTO, "validToken");
        });
        assertEquals("Region Not Found", exception.getMessage());
    }
    @Test
    void testUpdate_ThrowsVerificationException() {
        RegionDTO regionDTO = new RegionDTO("1", "UpdatedRegion", true);
        when(regionDAO.findById(regionDTO.getRegionId())).thenReturn(Optional.of(new Region()));
        when(verificationUtility.isValid(anyString())).thenReturn(false);
        Verfication exception = assertThrows(Verfication.class, () -> {
            regionService.update(regionDTO, "invalidToken");
        });
        assertEquals("Admin verification failed.", exception.getMessage());
    }
    @Test
    void testSetRegionStatus_Success_Enable() {
        RegionStatusDTO regionStatusDTO = new RegionStatusDTO("1", true);
        Region region = new Region("1", "Region", false);
        when(regionDAO.findById(regionStatusDTO.getRegionId())).thenReturn(Optional.of(region));
        when(verificationUtility.isValid(anyString())).thenReturn(true);
        ApiResponse<Object> result = regionService.setRegionStatus(regionStatusDTO, "validToken");
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertEquals("Region enabled successfully", result.getMessage());
        verify(regionDAO, times(1)).save(region);
    }
    @Test
    void testSetRegionStatus_Success_Disable() {
        RegionStatusDTO regionStatusDTO = new RegionStatusDTO("1", false);
        Region region = new Region("1", "Region", true);
        when(regionDAO.findById(regionStatusDTO.getRegionId())).thenReturn(Optional.of(region));
        when(verificationUtility.isValid(anyString())).thenReturn(true);
        ApiResponse<Object> result = regionService.setRegionStatus(regionStatusDTO, "validToken");
        assertEquals(HttpStatus.OK.value(), result.getStatus());
        assertEquals("Region disabled successfully", result.getMessage());
        verify(regionDAO, times(1)).save(region);
    }
    @Test
    void testSetRegionStatus_ThrowsVerificationException() {
        RegionStatusDTO regionStatusDTO = new RegionStatusDTO("1", true);
        when(verificationUtility.isValid(anyString())).thenReturn(false);
        Verfication exception = assertThrows(Verfication.class, () -> {
            regionService.setRegionStatus(regionStatusDTO, "invalidToken");
        });
        assertEquals("Admin verification failed.", exception.getMessage());
    }
    @Test
    void testSetRegionStatus_ThrowsValueNotFoundException() {
        RegionStatusDTO regionStatusDTO = new RegionStatusDTO("1", true);
        when(verificationUtility.isValid(anyString())).thenReturn(true);
        when(regionDAO.findById(regionStatusDTO.getRegionId())).thenReturn(Optional.empty());
        ValueNotFoundException exception = assertThrows(ValueNotFoundException.class, () -> {
            regionService.setRegionStatus(regionStatusDTO, "validToken");
        });
        assertEquals("Region not found for ID", exception.getMessage());
    }
}
