package com.ostia.productcatalogservice.common;

public enum LinkRelation {

    DELETE("delete"),
    UPDATE("update");

    private final String rel;

    LinkRelation(String rel) {
        this.rel = rel;
    }

    public String rel() {
        return rel;
    }
}
