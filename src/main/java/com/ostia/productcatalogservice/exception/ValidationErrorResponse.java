package com.ostia.productcatalogservice.exception;

import java.util.List;

public class ValidationErrorResponse extends ErrorResponse{

    private List<ValidationError> errors;

    public ValidationErrorResponse(int status, String error, String message, String path, List<ValidationError> errors) {
        super(status, error, message, path);
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
