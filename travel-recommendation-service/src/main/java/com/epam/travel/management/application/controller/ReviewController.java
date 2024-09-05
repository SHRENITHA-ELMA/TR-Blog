package com.epam.travel.management.application.controller;

import com.epam.travel.management.application.dto.ReviewRequest;
import com.epam.travel.management.application.entity.Review;
import com.epam.travel.management.application.entity.User;
import com.epam.travel.management.application.service.ReviewService;
import com.epam.travel.management.application.utility.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final TokenUtils tokenUtils;


    public ReviewController(ReviewService reviewService, TokenUtils tokenUtils) {
        this.reviewService = reviewService;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(HttpServletRequest request, @RequestBody ReviewRequest reviewRequest)
    {

        Optional<User> user = tokenUtils.getUserFromRequest(request);
        return ResponseEntity.ok(reviewService.createReview(reviewRequest.getDescription(),reviewRequest.getRating(),user.get()));
    }

}
