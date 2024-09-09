package com.admin_management_service.exceptions;

public class CountryNotFound extends RuntimeException{
    public CountryNotFound(String msg){
        super(msg);
    }
}
