package com.admin_management_service.utility;

import com.admin_management_service.feign.CountryFeign;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VerificationUtility {
    private CountryFeign countryFeign;

    public boolean isValid(String token){
        ResponseEntity<?> response = countryFeign.verifyAdmin(token);
        return response.getStatusCode() == HttpStatus.OK;
    }
}
