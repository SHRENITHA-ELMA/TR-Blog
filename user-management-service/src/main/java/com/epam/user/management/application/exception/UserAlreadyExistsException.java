package com.epam.user.management.application.exception;

public class UserAlreadyExistsException extends  RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
