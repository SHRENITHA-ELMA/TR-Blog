package com.epam.travel.management.application.repository;

import com.epam.travel.management.application.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> getBlogsByCategoryIdOrRegionIdOrCountryId(String categoryId,String regionId,String countryId);
}
