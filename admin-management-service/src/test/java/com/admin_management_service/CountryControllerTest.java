package com.admin_management_service;

import com.admin_management_service.controller.CountryController;
import com.admin_management_service.dto.*;
import com.admin_management_service.entity.Country;
import com.admin_management_service.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CountryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryController countryController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
    }

    @Test
    void testGetAllCountries_Success() throws Exception {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country());

        when(countryService.getall()).thenReturn(countryList);

        mockMvc.perform(get("/country/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("Countries fetched"))
                .andExpect(jsonPath("$.data").isArray());

        verify(countryService, times(1)).getall();
    }

    @Test
    void testAddCountry_Success() throws Exception {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountryCode("+1");
        countryDTO.setCountry("USA");

        String token = "valid-token";

        mockMvc.perform(post("/country/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(countryDTO))
                        .header("Token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("Added Successfully"));

        verify(countryService, times(1)).add(eq(countryDTO), eq(token));
    }

    @Test
    void testUpdateCountry_Success() throws Exception {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountryCode("+1");
        countryDTO.setCountry("USA");

        String token = "valid-token";

        mockMvc.perform(put("/country/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(countryDTO))
                        .header("Token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("Country Update Success"));

        verify(countryService, times(1)).update(eq(countryDTO), eq(token));
    }

    @Test
    void testDeleteCountry_Success() throws Exception {
        DeleteDTO deleteDTO = new DeleteDTO();
        deleteDTO.setCountryCode("+1");

        String token = "valid-token";
        Country deletedCountry = new Country();
        deletedCountry.setCountryCode("+1");
        deletedCountry.setCountryName("USA");

        when(countryService.delete(eq(deleteDTO), eq(token))).thenReturn(deletedCountry);

        mockMvc.perform(delete("/country/Country")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteDTO))
                        .header("Token", token))
                .andExpect(status().isNotFound());

    }
}
