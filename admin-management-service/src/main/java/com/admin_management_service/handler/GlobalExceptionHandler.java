package com.admin_management_service.handler;

import com.admin_management_service.dto.ResponseFormat;
import com.admin_management_service.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleEmptyCountryListException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");
        ResponseFormat responseFormat= ResponseFormat.builder().status("401").message(errorMessage).build();
        return ResponseEntity.badRequest().body(responseFormat);
    }

    @ExceptionHandler(CountryNotFound.class)
    public ResponseEntity<?>handleCountryNotFoundException(CountryNotFound ex){
        ResponseFormat responseFormat=ResponseFormat.builder().status("401").message(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(responseFormat);
    }

    @ExceptionHandler(CountryExists.class)
    public ResponseEntity<?>handleCountryExistsException(CountryExists ex){
        ResponseFormat responseFormat=ResponseFormat.builder().status("401").message(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(responseFormat);
    }

    @ExceptionHandler(NoCountriesPresent.class)
    public ResponseEntity<?>handleNoCountriesPresentException(NoCountriesPresent ex){
        ResponseFormat responseFormat=ResponseFormat.builder().status("401").message(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(responseFormat);
    }

    @ExceptionHandler(Verfication.class)
    public ResponseEntity<?>handleVerficationException(Verfication ex){
        ResponseFormat responseFormat=ResponseFormat.builder().status("403").message(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(responseFormat);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<?>handleInvalidStatusException(Verfication ex){
        ResponseFormat responseFormat=ResponseFormat.builder().status("403").message(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(responseFormat);
    }

    @ExceptionHandler(NullValueException.class)
    public ResponseEntity<?>handleNullValueException(NullValueException ex){
        ResponseFormat responseFormat=ResponseFormat.builder().status("401").message(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(responseFormat);
    }
    @ExceptionHandler(ValueExistsException.class)
    public ResponseEntity<?>handleValueExistsException(ValueExistsException ex){
        ResponseFormat responseFormat=ResponseFormat.builder().status("401").message(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(responseFormat);
    }
    @ExceptionHandler(ValueNotFoundException.class)
    public ResponseEntity<?>handleValueNotFoundException(ValueNotFoundException ex){
        ResponseFormat responseFormat=ResponseFormat.builder().status("401").message(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(responseFormat);
    }

}
