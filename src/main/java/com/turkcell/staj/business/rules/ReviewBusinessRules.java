package com.turkcell.staj.business.rules;

import com.turkcell.staj.entities.Review;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;

@Service
public final class ReviewBusinessRules {

    public static double calculateOfferAvgRating(List<Review> reviews) {
        OptionalDouble average = reviews.stream()
                .mapToInt(Review::getRating)
                .average();
        return average.orElse(0.0);
    }
}
