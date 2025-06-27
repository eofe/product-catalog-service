package com.ostia.productcatalogservice.service;

import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.mapper.DomainMapper;
import com.ostia.productcatalogservice.repository.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public UUID addCategory(CategoryDTO categoryDTO) {
        var category = DomainMapper.DTOToEntity(categoryDTO);

        if(!categoryRepository.existsByName(category.getName())) {
            var savedCategory =  categoryRepository.save(category);
            return savedCategory.getId();
        }

        throw new EntityExistsException("Category already exists");
    }
}
