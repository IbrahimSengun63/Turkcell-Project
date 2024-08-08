package com.turkcell.staj.business.rules;

import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.entities.Review;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.OptionalDouble;

@Service
public final class ReviewBusinessRules {


    public static double calculateOfferAvgRating(List<Review> reviews) {
        OptionalDouble average = reviews.stream()
                .mapToInt(Review::getRating)
                .average();
        double avg = average.orElse(0.0);
        // Use BigDecimal for precise rounding to two decimal places
        return (BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP)).doubleValue();
    }

    public static void assertIfUserPurchasedOffer(boolean isPurchased) {
        if (!isPurchased) {
            throw new BusinessException("User can not add a review to an offer that is not purchased");
        }
    }
}
