package com.epam.travel.management.application.exceptions;

public class UserAlreadyExistsException extends  RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
