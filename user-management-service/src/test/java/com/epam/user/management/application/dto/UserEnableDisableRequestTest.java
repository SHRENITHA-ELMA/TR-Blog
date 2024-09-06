package com.epam.user.management.application.dto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
public class UserEnableDisableRequestTest {
    private static Validator validator;
    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    public void whenEnableIsNull_thenValidationFails() {
        UserEnableDisableRequest request = new UserEnableDisableRequest(null);
        Set<ConstraintViolation<UserEnableDisableRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        ConstraintViolation<UserEnableDisableRequest> violation = violations.iterator().next();
        assertEquals("Enable field is required ", violation.getMessage());
    }
    @Test
    public void whenEnableIsNotNull_thenValidationSucceeds() {
        UserEnableDisableRequest request = new UserEnableDisableRequest(true);
        Set<ConstraintViolation<UserEnableDisableRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}
