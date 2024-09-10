package com.epam.travel.management.application.exceptions;

public class InvalidFileFormatException extends RuntimeException{
    public InvalidFileFormatException(String message)
    {
        super(message);
    }
}
