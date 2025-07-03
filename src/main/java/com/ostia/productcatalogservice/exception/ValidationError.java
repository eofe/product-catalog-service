package com.ostia.productcatalogservice.exception;

public record ValidationError(String field, String message) {
}