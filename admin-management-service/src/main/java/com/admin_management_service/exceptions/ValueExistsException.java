package com.admin_management_service.exceptions;

public class ValueExistsException extends RuntimeException{
    public ValueExistsException(String message) {
        super(message);
    }
}
