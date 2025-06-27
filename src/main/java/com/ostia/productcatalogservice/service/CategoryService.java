package com.ostia.productcatalogservice.service;

import com.ostia.productcatalogservice.dto.CategoryDTO;
import java.util.UUID;

public interface CategoryService {

    UUID addCategory(CategoryDTO categoryDTO);
}