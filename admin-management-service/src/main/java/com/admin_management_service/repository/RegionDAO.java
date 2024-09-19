package com.admin_management_service.repository;
import com.admin_management_service.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionDAO extends JpaRepository<Region,String> {
}
