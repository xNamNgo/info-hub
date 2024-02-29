package com.info_hub.exceptions;

public class NotFoundExcept extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private int fieldValue;

    public NotFoundExcept(String resourceName, String fieldName, int fieldValue) {
        super(String.format("%s not found with %s : '%s'",resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
