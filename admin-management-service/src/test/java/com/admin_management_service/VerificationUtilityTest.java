package com.admin_management_service;

import com.admin_management_service.feign.CountryFeign;
import com.admin_management_service.utility.VerificationUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerificationUtilityTest {

    @Mock
    private CountryFeign countryFeign;

    @InjectMocks
    private VerificationUtility verificationUtility;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsValid_Success() {
        String token = "valid-token";

        // Mock the CountryFeign to return an OK response
        when(countryFeign.verifyAdmin(token)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Call the method
        boolean result = verificationUtility.isValid(token);

        // Verify the result
        assertTrue(result);
    }

    @Test
    void testIsValid_Failure() {
        String token = "invalid-token";

        // Mock the CountryFeign to return a FORBIDDEN response
        when(countryFeign.verifyAdmin(token)).thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));

        // Call the method
        boolean result = verificationUtility.isValid(token);

        // Verify the result
        assertFalse(result);
    }

    @Test
    void testIsValid_Failure_BadRequest() {
        String token = "bad-request-token";

        // Mock the CountryFeign to return a BAD_REQUEST response
        when(countryFeign.verifyAdmin(token)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Call the method
        boolean result = verificationUtility.isValid(token);

        // Verify the result
        assertFalse(result);
    }
}
