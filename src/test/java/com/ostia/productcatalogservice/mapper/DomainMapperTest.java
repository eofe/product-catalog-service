package com.ostia.productcatalogservice.mapper;

import com.ostia.productcatalogservice.dto.CategoryDTO;
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
    void should() {

        // Act & Assert
        assertThatThrownBy(() -> DomainMapper.DTOToEntity(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The category DTO is null");
    }
}