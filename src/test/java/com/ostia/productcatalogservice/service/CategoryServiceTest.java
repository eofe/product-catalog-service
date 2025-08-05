package com.ostia.productcatalogservice.service;

import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.dto.UpdateCategoryDTO;
import com.ostia.productcatalogservice.exception.EntityAlreadyExistsException;
import com.ostia.productcatalogservice.exception.EntityNotFoundException;
import com.ostia.productcatalogservice.mapper.DomainMapper;
import com.ostia.productcatalogservice.model.Category;
import com.ostia.productcatalogservice.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @Test
    void shouldAddCategoryWhenItDoesNotExist() {
        // Given
        CategoryDTO dto = new CategoryDTO("Books", "Books desc");
        Category entity = DomainMapper.DTOToEntity(dto);


        try (MockedStatic<DomainMapper> mockMapper = mockStatic(DomainMapper.class)) {
            mockMapper.when(() -> DomainMapper.DTOToEntity(dto)).thenReturn(entity);
            when(categoryRepository.existsByName("Books")).thenReturn(false);
            when(categoryRepository.save(entity)).thenReturn(entity);

            // When
            UUID result = categoryService.addCategory(dto);

            // Then
            assertThat(result).isEqualTo(entity.getId());
            verify(categoryRepository).save(entity);
        }
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists() {
        // Given
        CategoryDTO dto = new CategoryDTO("Books", "Books desc");

        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());

        // Mock static mapping
        try (MockedStatic<DomainMapper> mockedMapper = mockStatic(DomainMapper.class)) {
            mockedMapper.when(() -> DomainMapper.DTOToEntity(dto)).thenReturn(category);
            when(categoryRepository.existsByName("Books")).thenReturn(true);

            // When + Then
            assertThatThrownBy(() -> categoryService.addCategory(dto))
                    .isInstanceOf(EntityAlreadyExistsException.class)
                    .hasMessage("Category with name 'Books' already exists");

            verify(categoryRepository, never()).save(any());
        }
    }

    @Test
    void shouldGetCategoryWhenCategoryExists() {
        // Given
        Category category = new Category();
        category.setName("Books");
        category.setDescription("Books desc");

        CategoryDTO dto = DomainMapper.EntityToDTO(category);

        try (MockedStatic<DomainMapper> mockMapper = mockStatic(DomainMapper.class)) {
            mockMapper.when(() -> DomainMapper.EntityToDTO(category)).thenReturn(dto);
            when(categoryRepository.existsByName("Books")).thenReturn(true);
            when(categoryRepository.findByName("Books")).thenReturn(category);

            // When
            var retrievedCategory = categoryService.getCategory("Books");

            // Then
            assertThat(retrievedCategory).isNotNull();
            assertThat(retrievedCategory.name()).isEqualTo("Books");
            assertThat(retrievedCategory.description()).isEqualTo("Books desc");

            verify(categoryRepository).existsByName("Books");
            verify(categoryRepository).findByName("Books");
        }
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenCategoryDoesNotExist() {
        // Given
        String categoryName = "Books";
        when(categoryRepository.existsByName(categoryName)).thenReturn(false);

        // When + Then
        assertThatThrownBy(() -> categoryService.getCategory(categoryName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category entity with name 'Books' does not exist");

        verify(categoryRepository).existsByName(categoryName);
        verify(categoryRepository, never()).findByName(any());
    }

    @Test
    void shouldUpdateCategoryDescription() {
        // Given
        String name = "Books";
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName(name);
        category.setDescription("Old description");

        when(categoryRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(category));

        UpdateCategoryDTO updateDTO = new UpdateCategoryDTO("New updated description");

        // When
        categoryService.updateCategory(name, updateDTO);

        // Then
        assertThat(category.getDescription()).isEqualTo("New updated description");
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldReturnPagedCategoryDTOs() {
        // Given
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Music");
        category.setDescription("All music items");

        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        // When
        Page<CategoryDTO> result = categoryService.getAllCategories(pageable);

        Assertions.assertThat(result.getContent()).hasSize(1);
        // Then
        assertThat(result.getContent().get(0).name()).isEqualTo("Music");
        assertThat(result.getContent().get(0).description()).isEqualTo("All music items");
    }
}