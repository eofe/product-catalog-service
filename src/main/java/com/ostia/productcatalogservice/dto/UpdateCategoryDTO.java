package com.ostia.productcatalogservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryDTO(@NotBlank String description) {
}
