package com.ostia.productcatalogservice.controller;

import com.ostia.productcatalogservice.common.ApiVersion;
import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping(ApiVersion.V1 + "/categories")
public class CategoryController {
    public static final String CATEGORIES_PATH = "categories/";
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody CategoryDTO categoryDTO, UriComponentsBuilder ucb) {

        var id = categoryService.addCategory(categoryDTO);
        URI locationOfNewCategory = ucb
                .path(CATEGORIES_PATH + id)
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(locationOfNewCategory).build();
    }
}
