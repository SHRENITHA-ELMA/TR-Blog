package com.epam.travel.management.application.repository;

import com.epam.travel.management.application.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> getBlogsByCategoryIdOrRegionIdOrCountryId(String categoryId,String regionId,String countryId);
    List<Blog> findByStatus(String status);
    @Query("SELECT b FROM Blog b WHERE b.status = 'accept' " +
            "AND (:regionId IS NULL OR b.regionId = :regionId) " +
            "AND (:categoryId IS NULL OR b.categoryId = :categoryId) " +
            "AND (:countryId IS NULL OR b.countryId = :countryId)")
    List<Blog> findByStatusAndFilters(@Param("regionId") String regionId,
                                      @Param("categoryId") String categoryId,
                                      @Param("countryId") String countryId);

}
