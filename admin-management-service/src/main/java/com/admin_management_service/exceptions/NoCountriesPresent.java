package com.admin_management_service.exceptions;

public class NoCountriesPresent extends RuntimeException{
    public NoCountriesPresent(String message){
        super(message);
    }
}
