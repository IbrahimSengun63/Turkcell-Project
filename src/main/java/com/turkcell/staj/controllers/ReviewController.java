package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.requests.RequestUpdateReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllUserReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseUpdateReviewDTO;
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
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/offer/{offerId}")
    @Cacheable(value = "offer_review_list")
    public ResponseEntity<GetOfferReviewsWrapper> getAllOfferReviews(@PathVariable @Valid @Min(value = 1) int offerId) {
        GetOfferReviewsWrapper wrapper = reviewService.getAllOfferReviews(offerId);
        return ResponseEntity.ok(wrapper);
    }

    @GetMapping("/user/{userId}")
    @Cacheable(value = "user_review_list")
    public ResponseEntity<List<ResponseGetAllUserReviewDTO>> getAllUserReviews(@PathVariable @Valid @Min(value = 1) int userId) {
        List<ResponseGetAllUserReviewDTO> responses = reviewService.getAllUserReviews(userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/add")
    @CacheEvict(value = {"offer_review_list","user_review_list"}, allEntries = true)
    public ResponseEntity<ResponseAddReviewDTO> addReview(@Valid @RequestBody RequestAddReviewDTO request) {
        ResponseAddReviewDTO response = reviewService.addReview(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = {"offer_review_list","user_review_list"}, allEntries = true)
    public ResponseEntity<ResponseUpdateReviewDTO> updateReview(@PathVariable @Valid int id, @Valid @RequestBody RequestUpdateReviewDTO request) {
        ResponseUpdateReviewDTO response = reviewService.updateReview(id,request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public ResponseEntity<ResponseGetReviewDTO> getReview(@PathVariable @Valid int id) {
        ResponseGetReviewDTO response = reviewService.getReview(id);
        return ResponseEntity.ok(response);
    }
}
