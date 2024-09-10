package com.epam.travel.management.application.exceptions;

public class EmailMismatchException extends RuntimeException{
    public EmailMismatchException(String message)
    {
        super(message);
    }
}
