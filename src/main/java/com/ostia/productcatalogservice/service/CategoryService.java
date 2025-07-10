package com.ostia.productcatalogservice.service;

import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.exception.EntityAlreadyExistsException;
import com.ostia.productcatalogservice.exception.EntityNotFoundException;
import com.ostia.productcatalogservice.mapper.DomainMapper;
import com.ostia.productcatalogservice.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public UUID addCategory(CategoryDTO categoryDTO) {
        var category = DomainMapper.DTOToEntity(categoryDTO);

        if(!categoryRepository.existsByName(category.getName())) {
            var savedCategory =  categoryRepository.save(category);
            return savedCategory.getId();
        }

        throw new EntityAlreadyExistsException("Category", "name", category.getName());
    }

    public CategoryDTO getCategory(String catName) {

        if (categoryRepository.existsByName(catName)) {
            return DomainMapper.EntityToDTO(categoryRepository.findByName(catName));
        }
        throw new EntityNotFoundException("Category", "name", catName);
    }
}
