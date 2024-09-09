package com.epam.user.management.application.dto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
class UserEnableDisableRequestTest {
    private Validator validator;
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void testValidUserEnableDisableRequest() {
        UserEnableDisableRequest request = new UserEnableDisableRequest(1L, true);
        Set<ConstraintViolation<UserEnableDisableRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
    @Test
    void testUserEnableDisableRequest_NullId() {
        UserEnableDisableRequest request = new UserEnableDisableRequest(null, true);
        Set<ConstraintViolation<UserEnableDisableRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        ConstraintViolation<UserEnableDisableRequest> violation = violations.iterator().next();
        assertEquals("Id field is required", violation.getMessage());
        assertEquals("id", violation.getPropertyPath().toString());
    }
    @Test
    void testUserEnableDisableRequest_NullEnable() {
        UserEnableDisableRequest request = new UserEnableDisableRequest(1L, null);
        Set<ConstraintViolation<UserEnableDisableRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        ConstraintViolation<UserEnableDisableRequest> violation = violations.iterator().next();
        assertEquals("Enable field is required", violation.getMessage());
        assertEquals("enable", violation.getPropertyPath().toString());
    }
    @Test
    void testUserEnableDisableRequest_AllFieldsNull() {
        UserEnableDisableRequest request = new UserEnableDisableRequest(null, null);
        Set<ConstraintViolation<UserEnableDisableRequest>> violations = validator.validate(request);
        assertEquals(2, violations.size());
        for (ConstraintViolation<UserEnableDisableRequest> violation : violations) {
            if (violation.getPropertyPath().toString().equals("id")) {
                assertEquals("Id field is required", violation.getMessage());
            } else if (violation.getPropertyPath().toString().equals("enable")) {
                assertEquals("Enable field is required", violation.getMessage());
            }
        }
    }
}
