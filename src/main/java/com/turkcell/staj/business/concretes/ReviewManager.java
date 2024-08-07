package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.business.rules.ReviewBusinessRules;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllOfferReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllUserReviewDTO;
import com.turkcell.staj.entities.Review;
import com.turkcell.staj.mappers.ReviewMapper;
import com.turkcell.staj.repositories.OfferRepository;
import com.turkcell.staj.repositories.ReviewRepository;
import com.turkcell.staj.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewManager implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;


    @Override
    public GetOfferReviewsWrapper getAllOfferReviews(int offerId) {
        this.offerRepository.findById(offerId).orElseThrow(() -> new BusinessException("Offer can't be null"));
        List<Review> reviews = this.reviewRepository.findByOfferId(offerId);
        double avgRating = ReviewBusinessRules.calculateOfferAvgRating(reviews);
        List<ResponseGetAllOfferReviewDTO> responses = this.reviewMapper.reviewsToResponseGetAllOfferReviewsDto(reviews);
        GetOfferReviewsWrapper wrapper = new GetOfferReviewsWrapper();
        wrapper.setOfferReviews(responses);
        wrapper.setOfferAvgRating(avgRating);
        return wrapper;
    }

    @Override
    public List<ResponseGetAllUserReviewDTO> getAllUserReviews(int userId) {
        this.userRepository.findById(userId).orElseThrow(() -> new BusinessException("User can't be null"));
        List<Review> reviews = this.reviewRepository.findByUserId(userId);
        return this.reviewMapper.reviewsToResponseGetAllUserReviewsDto(reviews);
    }
}
