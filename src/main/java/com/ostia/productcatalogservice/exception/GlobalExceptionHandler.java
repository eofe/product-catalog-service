package com.ostia.productcatalogservice.exception;

import com.ostia.productcatalogservice.util.CustomMessageResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final CustomMessageResolver messages;

    public GlobalExceptionHandler(CustomMessageResolver messages) {
        this.messages = messages;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error(messages.get("log.malformed.json"), ex.getMessage());

        return buildErrorResponse(
                request,
                HttpStatus.BAD_REQUEST,
                messages.get("error.malformed.json")
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error(messages.get("log.validation.method.argument.failed"),
                Objects.requireNonNull(ex.getParameter().getMethod()).toGenericString(),
                ex.getParameter().getParameterIndex(),
                ex.getBindingResult().getFieldErrors()
        );

        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return buildErrorResponse(
                request,
                HttpStatus.UNPROCESSABLE_ENTITY,
                messages.get("error.validation.failed"),
                errors
        );
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExists(EntityAlreadyExistsException ex, HttpServletRequest request) {
        log.error(messages.get("log.entity.exists"), ex.getMessage());

        return buildErrorResponse(
                request,
                HttpStatus.CONFLICT,
                messages.get("error.entity.exists", ex.getEntityName(), ex.getFieldName(), ex.getFieldValue())
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        log.error(messages.get("log.entity.notfound"), ex.getEntityName(), ex.getFieldName(), ex.getFieldValue());

        return buildErrorResponse(
                request,
                HttpStatus.NOT_FOUND,
                messages.get("error.entity.notfound", ex.getEntityName(), ex.getFieldName(), ex.getFieldValue())
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpServletRequest request, HttpStatus status, String message, List<ValidationError> errors) {
        ValidationErrorResponse response = new ValidationErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.status(status).body(response);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpServletRequest request, HttpStatus status, String message) {
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }
}
