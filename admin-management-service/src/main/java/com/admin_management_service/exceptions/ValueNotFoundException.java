package com.admin_management_service.exceptions;

public class ValueNotFoundException extends RuntimeException{
    public ValueNotFoundException(String message) {
        super(message);
    }
}
