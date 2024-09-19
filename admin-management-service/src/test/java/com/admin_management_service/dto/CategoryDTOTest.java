package com.admin_management_service.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryDTOTest {

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO();
    }

    @Test
    void testDefaultConstructor() {
        assertThat(categoryDTO).isNotNull();
        assertThat(categoryDTO.getCategoryId()).isNull();
        assertThat(categoryDTO.getCategoryName()).isNull();
        assertThat(categoryDTO.isEnabled()).isFalse();
    }

    @Test
    void testParameterizedConstructor() {
        String categoryId = "CAT001";
        String categoryName = "Electronics";
        boolean enabled = true;

        categoryDTO = new CategoryDTO(categoryId, categoryName, enabled);

        assertThat(categoryDTO.getCategoryId()).isEqualTo(categoryId);
        assertThat(categoryDTO.getCategoryName()).isEqualTo(categoryName);
        assertThat(categoryDTO.isEnabled()).isEqualTo(enabled);
    }

    @Test
    void testGettersAndSetters() {
        categoryDTO.setCategoryId("CAT002");
        categoryDTO.setCategoryName("Books");
        categoryDTO.setEnabled(true);

        assertThat(categoryDTO.getCategoryId()).isEqualTo("CAT002");
        assertThat(categoryDTO.getCategoryName()).isEqualTo("Books");
        assertThat(categoryDTO.isEnabled()).isTrue();
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId("CAT003");
        dto.setCategoryName("Clothing");
        dto.setEnabled(false);

        assertThat(dto.getCategoryId()).isEqualTo("CAT003");
        assertThat(dto.getCategoryName()).isEqualTo("Clothing");
        assertThat(dto.isEnabled()).isFalse();
    }
}
