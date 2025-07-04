package com.ostia.productcatalogservice.exception;

public class EntityAlreadyExistsException  extends RuntimeException{
    private final String entityName;
    private final String fieldName;
    private final String fieldValue;

    public EntityAlreadyExistsException(String entityName, String fieldName, String fieldValue) {
        super(String.format("%s with %s '%s' already exists", entityName, fieldName, fieldValue));
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
