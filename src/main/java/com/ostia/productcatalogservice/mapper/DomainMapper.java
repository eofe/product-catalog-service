package com.ostia.productcatalogservice.mapper;

import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.model.Category;
import java.util.Objects;

public class DomainMapper {

    public static Category DTOToEntity(CategoryDTO categoryDTO) {

        if(Objects.isNull(categoryDTO)) {
            throw new IllegalArgumentException("The category DTO is null");
        }

        Category category = new Category();
        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());

        return category;
    }

    public static CategoryDTO EntityToDTO(Category category) {

        if(Objects.isNull(category)) {
            throw new IllegalArgumentException("The category entity is null");
        }

        return new CategoryDTO(category.getName(), category.getDescription());
    }
}