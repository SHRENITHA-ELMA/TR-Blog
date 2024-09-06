package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.RegisterRequest;
import com.epam.user.management.application.exception.UserAlreadyExistsException;
import com.epam.user.management.application.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticationService authenticationService;

	@Test
	public void testRegister_Success() throws Exception {
		// Create a RegisterRequest object
		RegisterRequest request = new RegisterRequest();
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setEmail("johndoe@example.com");
		request.setPassword("password");
		request.setGender("Male");
		request.setCountry("USA");
		request.setCity("New York");

		// Mock the service response (returning a String)
		when(authenticationService.register(any(RegisterRequest.class)))
				.thenReturn("User registration successful");

		// Perform POST request and validate response
		mockMvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) jsonPath("$.status").value(200))
				.andExpect((ResultMatcher) jsonPath("$.message").value("User registration successful"));
	}

	@Test
	public void testRegister_UserAlreadyExists() throws Exception {
		RegisterRequest request = new RegisterRequest();
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setEmail("johndoe@example.com");
		request.setPassword("password");
		request.setGender("Male");
		request.setCountry("USA");
		request.setCity("New York");

		// Mock service to throw exception
		when(authenticationService.register(any(RegisterRequest.class)))
				.thenThrow(new UserAlreadyExistsException("User with this email already exists"));

		mockMvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isConflict())  // Assuming you return 409 for UserAlreadyExists
				.andExpect((ResultMatcher) jsonPath("$.message").value("User with this email already exists"));
	}
}

