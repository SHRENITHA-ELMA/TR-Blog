package com.epam.user.management.application.exception;

public class InvalidFileFormatException extends RuntimeException{
    public InvalidFileFormatException(String message)
    {
        super(message);
    }
}
