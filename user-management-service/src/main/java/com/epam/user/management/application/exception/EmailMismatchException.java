package com.epam.user.management.application.exception;

public class EmailMismatchException extends RuntimeException{
    public EmailMismatchException(String message)
    {
        super(message);
    }
}
