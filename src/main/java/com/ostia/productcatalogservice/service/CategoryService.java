package com.ostia.productcatalogservice.service;

import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.exception.EntityAlreadyExistsException;
import com.ostia.productcatalogservice.mapper.DomainMapper;
import com.ostia.productcatalogservice.repository.CategoryRepository;
import com.ostia.productcatalogservice.util.MessageResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageResolver messages;

    public CategoryService(CategoryRepository categoryRepository, MessageResolver messages) {
        this.categoryRepository = categoryRepository;
        this.messages = messages;
    }

    public UUID addCategory(CategoryDTO categoryDTO) {
        var category = DomainMapper.DTOToEntity(categoryDTO);

        if(!categoryRepository.existsByName(category.getName())) {
            var savedCategory =  categoryRepository.save(category);
            return savedCategory.getId();
        }

        throw new EntityAlreadyExistsException("Category", "name", category.getName());
    }
}
