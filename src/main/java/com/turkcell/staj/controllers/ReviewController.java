package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllUserReviewDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/v1/reviews/offer/{offerId}")
    public ResponseEntity<GetOfferReviewsWrapper> getAllOfferReviews(@PathVariable @Valid @Min(value = 1) int offerId) {
        GetOfferReviewsWrapper wrapper = this.reviewService.getAllOfferReviews(offerId);
        return ResponseEntity.ok(wrapper);
    }

    @GetMapping("/v1/reviews/user/{userId}")
    public ResponseEntity<List<ResponseGetAllUserReviewDTO>> getAllUserReviews(@PathVariable @Valid @Min(value = 1) int userId) {
        List<ResponseGetAllUserReviewDTO> responses = this.reviewService.getAllUserReviews(userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/v1/reviews/add")
    public ResponseEntity<ResponseAddReviewDTO> addReview(@Valid @RequestBody RequestAddReviewDTO request) {
        ResponseAddReviewDTO response = this.reviewService.addReview(request);
        return ResponseEntity.ok(response);
    }

}
