package com.admin_management_service;

import com.admin_management_service.dto.ResponseFormat;
import com.admin_management_service.exceptions.CountryExists;
import com.admin_management_service.exceptions.CountryNotFound;
import com.admin_management_service.exceptions.NoCountriesPresent;
import com.admin_management_service.exceptions.Verfication;
import com.admin_management_service.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleEmptyCountryListException() {
        // Mocking MethodArgumentNotValidException
        BindingResult bindingResult = new BindException(new Object(), "target");
        bindingResult.addError(new ObjectError("countryCode", "Country Code is must"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Call the handler method
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleEmptyCountryListException(ex);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseFormat responseBody = (ResponseFormat) responseEntity.getBody();
        assertEquals("401", responseBody.getStatus());
        assertEquals("Country Code is must", responseBody.getMessage());
    }

    @Test
    void testHandleCountryNotFoundException() {
        // Mocking CountryNotFound exception
        CountryNotFound ex = new CountryNotFound("Country Not Found");

        // Call the handler method
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleCountryNotFoundException(ex);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseFormat responseBody = (ResponseFormat) responseEntity.getBody();
        assertEquals("401", responseBody.getStatus());
        assertEquals("Country Not Found", responseBody.getMessage());
    }

    @Test
    void testHandleCountryExistsException() {
        // Mocking CountryExists exception
        CountryExists ex = new CountryExists("Country already exists");

        // Call the handler method
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleCountryExistsException(ex);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseFormat responseBody = (ResponseFormat) responseEntity.getBody();
        assertEquals("401", responseBody.getStatus());
        assertEquals("Country already exists", responseBody.getMessage());
    }

    @Test
    void testHandleNoCountriesPresentException() {
        // Mocking NoCountriesPresent exception
        NoCountriesPresent ex = new NoCountriesPresent("No countries available");

        // Call the handler method
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleNoCountriesPresentException(ex);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseFormat responseBody = (ResponseFormat) responseEntity.getBody();
        assertEquals("401", responseBody.getStatus());
        assertEquals("No countries available", responseBody.getMessage());
    }

    @Test
    void testHandleVerficationException() {
        // Mocking Verfication exception
        Verfication ex = new Verfication("Verification failed");

        // Call the handler method
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleVerficationException(ex);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseFormat responseBody = (ResponseFormat) responseEntity.getBody();
        assertEquals("403", responseBody.getStatus());
        assertEquals("Verification failed", responseBody.getMessage());
    }
}
