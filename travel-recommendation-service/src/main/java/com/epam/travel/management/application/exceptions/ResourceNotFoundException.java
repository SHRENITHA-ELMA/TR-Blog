package com.epam.travel.management.application.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
            super(message);
    }
}
