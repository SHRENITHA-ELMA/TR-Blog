package com.admin_management_service.exceptions;

public class CountryExists extends RuntimeException{
    public CountryExists(String message){
        super(message);
    }
}
