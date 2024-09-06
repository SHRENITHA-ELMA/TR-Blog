package com.epam.user.management.application.dto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class UserCreateRequestTest {
    @Test
    public void testGetterAndSetter() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setFirstName("John");
        userCreateRequest.setLastName("Doe");
        userCreateRequest.setEmail("john.doe@example.com");
        userCreateRequest.setPassword("securepassword");
        userCreateRequest.setGender("Male");
        userCreateRequest.setCountry("USA");
        userCreateRequest.setRole("Admin");
        userCreateRequest.setCity("New York");
        assertEquals("John", userCreateRequest.getFirstName());
        assertEquals("Doe", userCreateRequest.getLastName());
        assertEquals("john.doe@example.com", userCreateRequest.getEmail());
        assertEquals("securepassword", userCreateRequest.getPassword());
        assertEquals("Male", userCreateRequest.getGender());
        assertEquals("USA", userCreateRequest.getCountry());
        assertEquals("Admin", userCreateRequest.getRole());
        assertEquals("New York", userCreateRequest.getCity());
    }
    @Test
    public void testEmptyObjectInitialization() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        assertNull(userCreateRequest.getFirstName());
        assertNull(userCreateRequest.getLastName());
        assertNull(userCreateRequest.getEmail());
        assertNull(userCreateRequest.getPassword());
        assertNull(userCreateRequest.getGender());
        assertNull(userCreateRequest.getCountry());
        assertNull(userCreateRequest.getRole());
        assertNull(userCreateRequest.getCity());
    }
}
