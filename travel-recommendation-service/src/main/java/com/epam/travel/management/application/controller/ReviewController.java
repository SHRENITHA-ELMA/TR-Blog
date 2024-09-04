package com.epam.travel.management.application.controller;

import com.epam.travel.management.application.dto.ReviewRequest;
import com.epam.travel.management.application.entity.Review;
import com.epam.travel.management.application.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest)
    {
        return ResponseEntity.ok(reviewService.createReview(reviewRequest.getUserId(),reviewRequest.getDescription(),reviewRequest.getRating()));
    }

}
