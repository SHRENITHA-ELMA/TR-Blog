package com.admin_management_service.dto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class RegionStatusDTOTest {
    @Test
    void testRegionStatusDTOConstructorAndGetters() {
        RegionStatusDTO dto = new RegionStatusDTO("region123", true);
        assertNotNull(dto, "DTO should not be null");
        assertEquals("region123", dto.getRegionId(), "Region ID should match");
        assertEquals(true, dto.getEnabled(), "Enabled field should match");
    }
    @Test
    void testRegionStatusDTOSetters() {
        RegionStatusDTO dto = new RegionStatusDTO();
        dto.setRegionId("region456");
        dto.setEnabled(false);
        assertEquals("region456", dto.getRegionId(), "Region ID should match");
        assertEquals(false, dto.getEnabled(), "Enabled field should match");
    }
    @Test
    void testRegionStatusDTOBuilder() {
        RegionStatusDTO dto = RegionStatusDTO.builder()
                .regionId("region789")
                .enabled(true)
                .build();
        assertNotNull(dto, "DTO should not be null");
        assertEquals("region789", dto.getRegionId(), "Region ID should match");
        assertEquals(true, dto.getEnabled(), "Enabled field should match");
    }
}
