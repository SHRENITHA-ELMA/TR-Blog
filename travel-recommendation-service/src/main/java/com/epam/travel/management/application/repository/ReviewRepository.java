package com.epam.travel.management.application.repository;

import com.epam.travel.management.application.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long > {
}
