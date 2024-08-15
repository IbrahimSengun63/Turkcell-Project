package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.requests.RequestUpdateReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllUserReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseUpdateReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CacheConfig(cacheNames = "reviews")
@Tag(name = "Review Controller", description = "Manage reviews in the system")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add")
    @CacheEvict(value = {"offer_review_list", "user_review_list"}, allEntries = true, beforeInvocation = false)
    @Operation(summary = "Add Review", description = "Adds a new review to the database.")
    public ResponseEntity<ResponseAddReviewDTO> addReview(@Valid @RequestBody RequestAddReviewDTO request) {
        ResponseAddReviewDTO response = reviewService.addReview(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offer/{offerId}")
    @Cacheable(value = "offer_review_list")
    @Operation(summary = "Get All Reviews for an Offer", description = "Retrieves all reviews for a specific offer based on the offer ID.")
    public ResponseEntity<GetOfferReviewsWrapper> getAllOfferReviews(@PathVariable @Valid @Min(value = 1) int offerId) {
        GetOfferReviewsWrapper wrapper = reviewService.getAllOfferReviews(offerId);
        return ResponseEntity.ok(wrapper);
    }

    @PutMapping("/update/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = {"offer_review_list", "user_review_list"}, allEntries = true)
    @Operation(summary = "Update Review", description = "Updates an existing review based on the provided review ID and update data.")
    public ResponseEntity<ResponseUpdateReviewDTO> updateReview(@PathVariable @Valid @Min(value = 1) int id, @Valid @RequestBody RequestUpdateReviewDTO request) {
        ResponseUpdateReviewDTO response = reviewService.updateReview(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    @Operation(summary = "Get Review by ID", description = "Retrieves a specific review based on the review ID.")
    public ResponseEntity<ResponseGetReviewDTO> getReview(@PathVariable @Valid @Min(value = 1) int id) {
        ResponseGetReviewDTO response = reviewService.getReview(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Cacheable(value = "user_review_list")
    @Operation(summary = "Get All Reviews by User", description = "Retrieves all reviews made by a specific user based on the user ID.")
    public ResponseEntity<List<ResponseGetAllUserReviewDTO>> getAllUserReviews(@PathVariable @Valid @Min(value = 1) int userId) {
        List<ResponseGetAllUserReviewDTO> responses = reviewService.getAllUserReviews(userId);
        return ResponseEntity.ok(responses);
    }
}
