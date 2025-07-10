package com.ostia.productcatalogservice.mapper;

import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.model.Category;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class DomainMapperTest {

    @Test
    void shouldMapDTOToEntityCorrectly() {

        // Arrange
        CategoryDTO dto = new CategoryDTO("Electronics", "Devices and gadgets");

        // Act
        var entity = DomainMapper.DTOToEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Electronics");
        assertThat(entity.getDescription()).isEqualTo("Devices and gadgets");
    }

    @Test
    void shouldThrowExceptionWhenDTOIsNull() {

        // Act & Assert
        assertThatThrownBy(() -> DomainMapper.DTOToEntity(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The category DTO is null");
    }

    @Test
    void shouldMapEntityToDTOCorrectly() {

        // Arrange
        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("Devices and gadgets");

        // Act
        var entity = DomainMapper.EntityToDTO(category);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.name()).isEqualTo("Electronics");
        assertThat(entity.description()).isEqualTo("Devices and gadgets");
    }

    @Test
    void shouldThrowExceptionWhenEntityIsNull() {

        // Act & Assert
        assertThatThrownBy(() -> DomainMapper.EntityToDTO(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The category entity is null");
    }
}