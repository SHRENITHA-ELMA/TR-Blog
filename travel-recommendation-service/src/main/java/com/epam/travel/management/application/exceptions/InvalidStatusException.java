package com.epam.travel.management.application.exceptions;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException(String message){
        super(message);
    }

}
