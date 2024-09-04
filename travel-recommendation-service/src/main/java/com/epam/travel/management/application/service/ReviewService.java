package com.epam.travel.management.application.service;

import com.epam.travel.management.application.entity.Review;
import com.epam.travel.management.application.entity.User;
import com.epam.travel.management.application.feignClient.UserClient;
import com.epam.travel.management.application.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final UserClient userClient;

    private final ReviewRepository reviewRepository;

    public ReviewService(UserClient userClient, ReviewRepository reviewRepository) {
        this.userClient = userClient;
        this.reviewRepository = reviewRepository;
    }

    public User getUserById(Long userId) {
        return userClient.getUserById(userId);
    }

    public Review createReview(Long userId , String description , double rating )
    {
        User user=userClient.getUserById(userId);

        Review review= Review.builder().userId(userId).userName(user.getFirstName()+user.getLastName()).imageUrl(user.getImageUrl()).description(description).rating(rating).build();

        return reviewRepository.save(review);
    }
}