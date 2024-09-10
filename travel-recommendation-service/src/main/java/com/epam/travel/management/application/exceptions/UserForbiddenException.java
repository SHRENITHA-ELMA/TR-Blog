package com.epam.travel.management.application.exceptions;

public class UserForbiddenException extends RuntimeException {

    public UserForbiddenException(String message) {
        super(message);
    }
}
