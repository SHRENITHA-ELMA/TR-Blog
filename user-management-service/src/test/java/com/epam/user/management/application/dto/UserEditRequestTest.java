package com.epam.user.management.application.dto;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class UserEditRequestTest {
    private UserEditRequest userEditRequest;
    @BeforeEach
    public void setUp() {
        userEditRequest = new UserEditRequest();
    }
    @Test
    public void testSettersAndGetters() {
        // Act: Set values using setters
        userEditRequest.setFirstName("Alice");
        userEditRequest.setLastName("Johnson");
        userEditRequest.setEmail("alice.johnson@example.com");
        userEditRequest.setGender("Female");
        userEditRequest.setCountry("UK");
        userEditRequest.setRole("User");
        userEditRequest.setCity("London");
        assertEquals("Alice", userEditRequest.getFirstName());
        assertEquals("Johnson", userEditRequest.getLastName());
        assertEquals("alice.johnson@example.com", userEditRequest.getEmail());
        assertEquals("Female", userEditRequest.getGender());
        assertEquals("UK", userEditRequest.getCountry());
        assertEquals("User", userEditRequest.getRole());
        assertEquals("London", userEditRequest.getCity());
    }
    @Test
    public void testNullValues() {
        assertNull(userEditRequest.getFirstName());
        assertNull(userEditRequest.getLastName());
        assertNull(userEditRequest.getEmail());
        assertNull(userEditRequest.getGender());
        assertNull(userEditRequest.getCountry());
        assertNull(userEditRequest.getRole());
        assertNull(userEditRequest.getCity());
    }
    @Test
    public void testFieldUpdates() {
        userEditRequest.setFirstName("Bob");
        userEditRequest.setLastName("Smith");
        userEditRequest.setEmail("bob.smith@example.com");
        userEditRequest.setFirstName("Robert");
        assertEquals("Robert", userEditRequest.getFirstName());
        assertEquals("Smith", userEditRequest.getLastName());
        assertEquals("bob.smith@example.com", userEditRequest.getEmail());
    }
}