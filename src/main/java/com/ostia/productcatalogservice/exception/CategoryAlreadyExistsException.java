package com.ostia.productcatalogservice.exception;

public class CategoryAlreadyExistsException extends RuntimeException{
    public CategoryAlreadyExistsException(String categoryName) {
        // TODO: Replace hardcoded category message with a generic format to support other entity types
        super(String.format("Category with name '%s' already exists", categoryName));
    }
}
