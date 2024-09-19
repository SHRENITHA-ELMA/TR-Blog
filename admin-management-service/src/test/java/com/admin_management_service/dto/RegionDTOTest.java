package com.admin_management_service.dto;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
class RegionDTOTest {
    @Test
    void testRegionDTOConstructorAndGettersSetters() {
        String regionId = "R123";
        String regionName = "North";
        boolean enabled = true;
        RegionDTO regionDTO = new RegionDTO(regionId, regionName, enabled);
        assertThat(regionDTO.getRegionId()).isEqualTo(regionId);
        assertThat(regionDTO.getRegionName()).isEqualTo(regionName);
        assertThat(regionDTO.isEnabled()).isEqualTo(enabled);
    }
    @Test
    void testSetters() {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionId("R456");
        regionDTO.setRegionName("South");
        regionDTO.setEnabled(false);
        assertThat(regionDTO.getRegionId()).isEqualTo("R456");
        assertThat(regionDTO.getRegionName()).isEqualTo("South");
        assertThat(regionDTO.isEnabled()).isFalse();
    }
    @Test
    void testDefaultConstructor() {
        RegionDTO regionDTO = new RegionDTO();
        assertThat(regionDTO.getRegionId()).isNull();
        assertThat(regionDTO.getRegionName()).isNull();
        assertThat(regionDTO.isEnabled()).isFalse();
    }
    @Test
    void testToString() {
        RegionDTO regionDTO = new RegionDTO("R789", "East", true);
        String toStringResult = regionDTO.toString();
        assertThat(toStringResult).contains("RegionDTO(regionId=R789, regionName=East, enabled=true)");
    }
}
