package com.epam.user.management.application.exception;

public class UserForbiddenException extends RuntimeException {

    public UserForbiddenException(String message) {
        super(message);
    }
}
