package com.admin_management_service.repository;

import com.admin_management_service.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryDAO extends JpaRepository<Country,String> {
    List<Country> findAll();
}
