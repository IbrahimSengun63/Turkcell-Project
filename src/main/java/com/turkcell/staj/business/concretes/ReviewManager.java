package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.business.rules.ReviewBusinessRules;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.requests.RequestUpdateReviewDTO;
import com.turkcell.staj.dtos.review.responses.*;
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
    private final ReviewMapper reviewMapper;
    private final TransactionService transactionService;
    private final OfferService offerService;
    private final UserService userService;


    @Override
    public GetOfferReviewsWrapper getAllOfferReviews(int offerId) {
        offerService.getOfferById(offerId);
        List<Review> reviews = reviewRepository.findByOfferId(offerId);
        double avgRating = ReviewBusinessRules.calculateOfferAvgRating(reviews);
        List<ResponseGetAllOfferReviewDTO> responses = reviewMapper.reviewsToResponseGetAllOfferReviewsDto(reviews);
        GetOfferReviewsWrapper wrapper = new GetOfferReviewsWrapper();
        wrapper.setOfferReviews(responses);
        wrapper.setOfferAvgRating(avgRating);
        return wrapper;
    }

    @Override
    public List<ResponseGetAllUserReviewDTO> getAllUserReviews(int userId) {
        // check user
        userService.getUserById(userId);
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviewMapper.reviewsToResponseGetAllUserReviewsDto(reviews);
    }

    @Override
    public ResponseAddReviewDTO addReview(RequestAddReviewDTO request) {
        userService.getUserById(request.getUserId());
        offerService.getOfferById(request.getOfferId());
        boolean result = transactionService.checkIfUserPurchasedOffer(request.getUserId(), request.getOfferId());
        ReviewBusinessRules.assertIfUserPurchasedOffer(result);
        Review review = reviewMapper.requestAddReviewDtoToReview(request);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.reviewToResponseAddReviewDTO(savedReview);
    }

    @Override
    public ResponseUpdateReviewDTO updateReview(int id, RequestUpdateReviewDTO request) {
        // Get review from db and make checks
        Review review = reviewRepository.findById(id).orElseThrow(() -> new BusinessException("Review can't be null"));
        userService.getUserById(request.getUserId());
        offerService.getOfferById(request.getOfferId());
        boolean result = transactionService.checkIfUserPurchasedOffer(request.getUserId(), request.getOfferId());
        ReviewBusinessRules.assertIfUserPurchasedOffer(result);
        reviewMapper.updateReviewFromRequestUpdateReviewDto(request, review);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.reviewToResponseUpdateReviewDTO(savedReview);
    }

    @Override
    public ResponseGetReviewDTO getReview(int id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new BusinessException("Review can't be null"));
        return reviewMapper.reviewToResponseGetReviewDto(review);
    }
}
