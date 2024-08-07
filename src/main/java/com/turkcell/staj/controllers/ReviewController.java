package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/v1/reviews/{offerId}")
    public ResponseEntity<GetOfferReviewsWrapper> getAllOfferReviews(@PathVariable @Valid @Min(value = 1) int offerId) {
        GetOfferReviewsWrapper wrapper = this.reviewService.getAllOfferReviews(offerId);
        return ResponseEntity.ok(wrapper);
    }

}
