package com.admin_management_service;

import com.admin_management_service.repository.CountryDAO;
import com.admin_management_service.dto.CountryDTO;
import com.admin_management_service.dto.DeleteDTO;
import com.admin_management_service.entity.Country;
import com.admin_management_service.exceptions.CountryExists;
import com.admin_management_service.exceptions.CountryNotFound;
import com.admin_management_service.exceptions.NoCountriesPresent;
import com.admin_management_service.exceptions.Verfication;
import com.admin_management_service.feign.CountryFeign;
import com.admin_management_service.service.CountryService;
import com.admin_management_service.utility.VerificationUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminManagementServiceApplicationTests {

	@Mock
	private CountryDAO countryDAO;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private VerificationUtility verificationUtility;

	@InjectMocks
	private CountryService countryService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddCountry_Success() {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setCountryCode("+1");
		countryDTO.setCountry("USA");

		String token = "valid-token";

		when(countryDAO.findById("+1")).thenReturn(Optional.empty());
		when(verificationUtility.isValid(token)).thenReturn(true);

		String result = countryService.add(countryDTO, token);

		assertEquals("Adding Success", result);
		verify(countryDAO).save(any(Country.class));
	}

	@Test
	void testAddCountry_CountryExists() {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setCountryCode("+1");
		countryDTO.setCountry("USA");

		String token = "valid-token";

		when(countryDAO.findById("+1")).thenReturn(Optional.of(new Country()));

		assertThrows(CountryExists.class, () -> countryService.add(countryDTO, token));
		verify(countryDAO, never()).save(any(Country.class));
	}

	@Test
	void testAddCountry_InvalidAdminToken() {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setCountryCode("+1");
		countryDTO.setCountry("USA");

		String token = "invalid-token";

		when(countryDAO.findById("+1")).thenReturn(Optional.empty());
		when(verificationUtility.isValid(token)).thenReturn(false);

		assertThrows(Verfication.class, () -> countryService.add(countryDTO, token));
		verify(countryDAO, never()).save(any(Country.class));
	}

	@Test
	void testGetAllCountries_Success() {
		List<Country> countryList = new ArrayList<>();
		countryList.add(new Country());

		when(countryDAO.findAll()).thenReturn(countryList);

		List<Country> result = countryService.getall();

		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	void testGetAllCountries_NoCountriesPresent() {
		when(countryDAO.findAll()).thenReturn(new ArrayList<>());
		assertThrows(NoCountriesPresent.class, () -> countryService.getall());
	}

	@Test
	void testUpdateCountry_Success() {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setCountryCode("+1");
		countryDTO.setCountry("USA");

		String token = "valid-token";

		Country country = new Country();
		when(countryDAO.findById("+1")).thenReturn(Optional.of(country));
		when(verificationUtility.isValid(token)).thenReturn(true);

		String result = countryService.update(countryDTO, token);

		assertEquals("Country Update Success", result);
		verify(countryDAO).save(country);
	}

	@Test
	void testUpdateCountry_NotFound() {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setCountryCode("+1");

		String token = "valid-token";

		when(countryDAO.findById("+1")).thenReturn(Optional.empty());

		assertThrows(CountryNotFound.class, () -> countryService.update(countryDTO, token));
		verify(countryDAO, never()).save(any(Country.class));
	}

	@Test
	void testUpdateCountry_InvalidAdminToken() {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setCountryCode("+1");
		countryDTO.setCountry("USA");

		String token = "invalid-token";

		Country country = new Country();
		when(countryDAO.findById("+1")).thenReturn(Optional.of(country));
		when(verificationUtility.isValid(token)).thenReturn(false);

		assertThrows(Verfication.class, () -> countryService.update(countryDTO, token));
		verify(countryDAO, never()).save(any(Country.class));
	}

	@Test
	void testDeleteCountry_Success() {
		DeleteDTO deleteDTO = new DeleteDTO();
		deleteDTO.setCountryCode("+1");

		String token = "valid-token";

		Country country = new Country();
		when(countryDAO.findById("+1")).thenReturn(Optional.of(country));
		when(verificationUtility.isValid(token)).thenReturn(true);

		Country result = countryService.delete(deleteDTO, token);

		assertNull(result);
	}

	@Test
	void testDeleteCountry_NotFound() {
		DeleteDTO deleteDTO = new DeleteDTO();
		deleteDTO.setCountryCode("+1");

		String token = "valid-token";

		when(countryDAO.findById("+1")).thenReturn(Optional.empty());

		assertThrows(CountryNotFound.class, () -> countryService.delete(deleteDTO, token));
		verify(countryDAO, never()).save(any(Country.class));
	}

	@Test
	void testDeleteCountry_InvalidAdminToken() {
		DeleteDTO deleteDTO = new DeleteDTO();
		deleteDTO.setCountryCode("+1");

		String token = "invalid-token";

		Country country = new Country();
		when(countryDAO.findById("+1")).thenReturn(Optional.of(country));
		when(verificationUtility.isValid(token)).thenReturn(false);

		assertThrows(Verfication.class, () -> countryService.delete(deleteDTO, token));
		verify(countryDAO, never()).save(any(Country.class));
	}

}
