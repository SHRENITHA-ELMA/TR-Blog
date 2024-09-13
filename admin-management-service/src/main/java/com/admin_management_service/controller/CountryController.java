package com.admin_management_service.controller;

import com.admin_management_service.dto.*;
import com.admin_management_service.entity.Country;
import com.admin_management_service.service.CountryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("country")
@AllArgsConstructor
public class CountryController {

    private final CountryService countryService;


    @GetMapping("countries")
    public ResponseEntity<?> getall(){
        List<Country> res=countryService.getall();
        GetAllCountries getAllCountries = GetAllCountries.builder().status("200").message("Countries fetched").data(res).build();
        return ResponseEntity.ok(getAllCountries);
    }

    @PostMapping("countries")
    public ResponseEntity<?> add(@Valid @RequestBody CountryDTO countryDTO,@RequestHeader("Token") String token){
        System.out.println(token);
        countryService.add(countryDTO,token);
        AddResponse Response=AddResponse.builder().status("200").message("Added Successfully").build();
        return ResponseEntity.ok(Response);
    }

    @PutMapping("countries")
    public  ResponseEntity<?> update(@Valid @RequestBody CountryDTO countryDTO,@RequestHeader("Token") String token){
        countryService.update(countryDTO,token);
        AddResponse addResponse=AddResponse.builder().status("200").message("Country Update Success").build();
        return ResponseEntity.ok(addResponse);
    }

    @DeleteMapping("countries")
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteDTO deleteDTO,@RequestHeader("Token") String token){
        Country country=countryService.delete(deleteDTO,token);
        DeleteResponse deleteResponse= DeleteResponse.builder().status("200").message("Country Deleted Successfully").data(country).build();
        return ResponseEntity.ok(deleteResponse);
    }

}
