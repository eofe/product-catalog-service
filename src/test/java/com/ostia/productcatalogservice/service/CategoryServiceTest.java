package com.ostia.productcatalogservice.service;

import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.exception.EntityAlreadyExistsException;
import com.ostia.productcatalogservice.exception.EntityNotFoundException;
import com.ostia.productcatalogservice.mapper.DomainMapper;
import com.ostia.productcatalogservice.model.Category;
import com.ostia.productcatalogservice.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
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
}