package com.admin_management_service.service;

import com.admin_management_service.constant.Messages;
import com.admin_management_service.repository.CountryDAO;
import com.admin_management_service.dto.CountryDTO;
import com.admin_management_service.dto.DeleteDTO;
import com.admin_management_service.entity.Country;
import com.admin_management_service.exceptions.CountryExists;
import com.admin_management_service.exceptions.CountryNotFound;
import com.admin_management_service.exceptions.NoCountriesPresent;
import com.admin_management_service.exceptions.Verfication;
import com.admin_management_service.utility.VerificationUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CountryService {

    private final CountryDAO countryDAO;
    private final ObjectMapper objectMapper;

    private VerificationUtility verificationUtility;

    public String add(CountryDTO countryDTO,String Token){
        if(countryDAO.findById(countryDTO.getCountryCode()).isPresent()){
            throw new CountryExists("Country already present");
        }
        if (!verificationUtility.isValid(Token)) {
            throw new Verfication(Messages.verficationMessage);
        }
        Country country=Country.builder().countryCode(countryDTO.getCountryCode()).countryName(countryDTO.getCountry()).build();
        countryDAO.save(country);
        return "Adding Success";
    }

    public List<Country> getall(){
        if(countryDAO.findAll().isEmpty()){
            throw new NoCountriesPresent("Add countries before Fetching");
        }
        return countryDAO.findAll();
    }

    public String update(CountryDTO countryDTO,String Token){
        Optional<Country> countryOpt = countryDAO.findById(countryDTO.getCountryCode());
        if (countryOpt.isEmpty()) {
            throw new CountryNotFound("Country Not Found");
        }
        if (!verificationUtility.isValid(Token)) {
            throw new Verfication("Admin verification failed.");
        }

        Country country = countryOpt.get();
        country.setCountryName(countryDTO.getCountry());
        countryDAO.save(country);
        return "Country Update Success";
    }

    public Country delete(DeleteDTO deleteDTO,String Token){
        if(!countryDAO.findById(deleteDTO.getCountryCode()).isPresent()){
            throw new CountryNotFound("Country Not Found");
        }
        if (!verificationUtility.isValid(Token)) {
            throw new Verfication("Admin verification failed.");
        }
        Country country=Country.builder().countryCode(deleteDTO.getCountryCode()).countryName(deleteDTO.getCountry()).isDisabled(true).build();
        return countryDAO.save(country);
    }
}
