package com.ostia.productcatalogservice.repository;

import com.ostia.productcatalogservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByName(String name);
    Category findByName(String name);
    void deleteByName(String name);
}