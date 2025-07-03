package com.ostia.productcatalogservice.service;

import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.exception.CategoryAlreadyExistsException;
import com.ostia.productcatalogservice.mapper.DomainMapper;
import com.ostia.productcatalogservice.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CategoryService {

    private final static Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public UUID addCategory(CategoryDTO categoryDTO) {
        var category = DomainMapper.DTOToEntity(categoryDTO);

        if(!categoryRepository.existsByName(category.getName())) {
            var savedCategory =  categoryRepository.save(category);
            return savedCategory.getId();
        }

        // TODO: Externalize this log message or make it generic to support other entity types
        log.error("Category already exists");
        throw new CategoryAlreadyExistsException(category.getName());
    }
}
