package com.admin_management_service.repository;

import com.admin_management_service.entity.Category;
import com.admin_management_service.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionDAO extends JpaRepository<Region,String> {
}
