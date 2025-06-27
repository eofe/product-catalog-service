package com.ostia.productcatalogservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldReturnTrueWhenCategoryWithNameExists() {
        boolean exists = categoryRepository.existsByName("Electronics");
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCategoryWithNameDoesNotExist() {

        boolean exists = categoryRepository.existsByName("NonExistingCategory");
        assertThat(exists).isFalse();
    }
}