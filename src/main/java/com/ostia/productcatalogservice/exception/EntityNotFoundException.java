package com.ostia.productcatalogservice.exception;

public class EntityNotFoundException extends RuntimeException {
    private final String entityName;
    private final String fieldName;
    private final String fieldValue;

    public EntityNotFoundException(String entityName, String fieldName, String fieldValue) {
        super(String.format("%s entity with %s '%s' does not exist", entityName, fieldName, fieldValue));
        this.entityName = entityName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}