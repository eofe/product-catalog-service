package com.ostia.productcatalogservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(@NotBlank String name,
                          @NotBlank String description) {
}