package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

	@Mock
	private AuthenticationService authenticationService;

	@Mock
	private JwtService jwtService;

	@InjectMocks
	private AuthenticationController authenticationController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRegister_Success() {
		// Arrange
		RegisterRequest registerRequest = RegisterRequest.builder()
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@example.com")
				.password("password123")
				.gender("Male")
				.country("USA")
				.city("New York")
				.build();

		when(authenticationService.register(registerRequest)).thenReturn("Registration Successful");


		// Act
		ResponseEntity<ApiResponse<Object>> response = authenticationController.register(registerRequest);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(200, response.getBody().getStatus());
		assertEquals("Registration Successful", response.getBody().getMessage());
		verify(authenticationService, times(1)).register(registerRequest);
	}

	@Test
	void testAuthenticate_Success() {
		// Arrange
		LoginRequest loginRequest = LoginRequest.builder()
				.email("john.doe@example.com")
				.password("password123")
				.build();

		LoginResponse loginResponse = LoginResponse.builder()
				.token("token")
				.expiresIn(3600L)
				.role("USER")
				.build();

		ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
				.status(HttpStatus.OK.value())
				.message("Login Successful")
				.data(loginResponse)
				.build();

		when(authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(apiResponse);

		// Act
		ResponseEntity<ApiResponse<LoginResponse>> response = authenticationController.authenticate(loginRequest);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(200, response.getBody().getStatus());
		assertEquals("Login Successful", response.getBody().getMessage());
		assertNotNull(response.getBody().getData());
		assertEquals("token", response.getBody().getData().getToken());
		assertEquals(3600L, response.getBody().getData().getExpiresIn());
		assertEquals("USER", response.getBody().getData().getRole());

		verify(authenticationService, times(1)).authenticate(loginRequest.getEmail(), loginRequest.getPassword());
	}

	@Test
	void testAuthenticate_Failure() {
		// Arrange
		LoginRequest loginRequest = LoginRequest.builder()
				.email("wrong.email@example.com")
				.password("wrongPassword")
				.build();

//		ApiResponse<LoginResponse> errorResponse = ApiResponse.<LoginResponse>builder()
//				.status(HttpStatus.UNAUTHORIZED.value())
//				.message("Invalid credentials")
//				.build();

		when(authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())).thenThrow(new RuntimeException("Invalid credentials"));

		// Act
		ResponseEntity<ApiResponse<LoginResponse>> response = authenticationController.authenticate(loginRequest);

		// Assert
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertEquals(401, response.getBody().getStatus());
		assertEquals("Invalid credentials", response.getBody().getMessage());

		verify(authenticationService, times(1)).authenticate(loginRequest.getEmail(), loginRequest.getPassword());
	}

	@Test
	void testLogout_Success() {
		// Arrange
		HttpServletRequest request = mock(HttpServletRequest.class);
		LogoutResponse logoutResponse = new LogoutResponse("Logout Successful", HttpStatus.OK.value());
		when(authenticationService.logout(request)).thenReturn(logoutResponse);

		// Act
		ResponseEntity<ApiResponse<LogoutResponse>> response = authenticationController.logout(request);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(200, response.getBody().getStatus());
		assertEquals("Logout Successful", response.getBody().getMessage());
		verify(authenticationService, times(1)).logout(request);
	}
}
