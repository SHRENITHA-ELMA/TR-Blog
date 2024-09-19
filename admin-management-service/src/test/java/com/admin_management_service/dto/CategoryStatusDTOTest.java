package com.admin_management_service.dto;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryStatusDTOTest {

    private CategoryStatusDTO categoryStatusDTO;

    @BeforeEach
    public void setUp() {
        categoryStatusDTO = new CategoryStatusDTO();
    }

    @Test
    public void testDefaultConstructor() {
        assertThat(categoryStatusDTO).isNotNull();
        assertThat(categoryStatusDTO.getCategoryId()).isNull();
        assertThat(categoryStatusDTO.getEnabled()).isNull();
    }

    @Test
    public void testParameterizedConstructor() {
        String categoryId = "CAT001";
        Boolean enabled = true;

        categoryStatusDTO = new CategoryStatusDTO(categoryId, enabled);

        assertThat(categoryStatusDTO.getCategoryId()).isEqualTo(categoryId);
        assertThat(categoryStatusDTO.getEnabled()).isEqualTo(enabled);
    }

    @Test
    public void testGettersAndSetters() {
        categoryStatusDTO.setCategoryId("CAT002");
        categoryStatusDTO.setEnabled(false);

        assertThat(categoryStatusDTO.getCategoryId()).isEqualTo("CAT002");
        assertThat(categoryStatusDTO.getEnabled()).isFalse();
    }

    @Test
    public void testBuilder() {
        CategoryStatusDTO dto = CategoryStatusDTO.builder()
                .categoryId("CAT003")
                .enabled(true)
                .build();

        assertThat(dto.getCategoryId()).isEqualTo("CAT003");
        assertThat(dto.getEnabled()).isTrue();
    }
}
