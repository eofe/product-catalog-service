package com.ostia.productcatalogservice.controller;

import com.ostia.productcatalogservice.assembler.CategoryModelAssembler;
import com.ostia.productcatalogservice.common.ApiVersion;
import com.ostia.productcatalogservice.dto.CategoryDTO;
import com.ostia.productcatalogservice.dto.UpdateCategoryDTO;
import com.ostia.productcatalogservice.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping(ApiVersion.V1 + "/categories")
public class CategoryController {
    public static final String CATEGORIES_PATH = "categories/";
    private final CategoryService categoryService;
    private final CategoryModelAssembler categoryModelAssembler;

    public CategoryController(CategoryService categoryService, CategoryModelAssembler categoryModelAssembler) {
        this.categoryService = categoryService;
        this.categoryModelAssembler = categoryModelAssembler;
    }

    @PreAuthorize("hasRole('PRODUCT_CATALOG_MANAGER')")
    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, UriComponentsBuilder ucb) {

        var id = categoryService.addCategory(categoryDTO);
        URI locationOfNewCategory = ucb
                .path(CATEGORIES_PATH + id)
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(locationOfNewCategory).build();
    }

    @PreAuthorize("hasRole('PRODUCT_CATALOG_MANAGER')")
    @GetMapping("/{name}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable String name) {

       return ResponseEntity.ok(categoryService.getCategory(name));
    }

    @PreAuthorize("hasRole('PRODUCT_CATALOG_MANAGER')")
    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@RequestParam String name) {
        categoryService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('PRODUCT_CATALOG_MANAGER')")
    @PutMapping("/{name}")
    public ResponseEntity<Void> updateCategory(@PathVariable String name,
                                               @Valid @RequestBody UpdateCategoryDTO dto) {
        categoryService.updateCategory(name, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('PRODUCT_CATALOG_MANAGER')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CategoryDTO>>> getAllCategories(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            PagedResourcesAssembler<CategoryDTO> assembler) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> result = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(assembler.toModel(result, categoryModelAssembler));
    }
}
