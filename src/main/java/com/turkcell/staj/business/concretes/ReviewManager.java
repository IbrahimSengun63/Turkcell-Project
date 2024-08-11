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
import com.turkcell.staj.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewManager implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final TransactionService transactionService;
    private final OfferService offerService;
    private final UserService userService;


    @Override
    public GetOfferReviewsWrapper getAllOfferReviews(int offerId) {
        offerService.getOfferById(offerId);
        log.info("Retrieved offer with ID {} from the database.", offerId);
        List<Review> reviews = reviewRepository.findByOfferId(offerId);
        log.info("Retrieved reviews with offerID {} from the database.", offerId);
        double avgRating = ReviewBusinessRules.calculateOfferAvgRating(reviews);
        List<ResponseGetAllOfferReviewDTO> responses = reviewMapper.reviewsToResponseGetAllOfferReviewsDto(reviews);
        GetOfferReviewsWrapper wrapper = new GetOfferReviewsWrapper();
        wrapper.setOfferReviews(responses);
        wrapper.setOfferAvgRating(avgRating);
        return wrapper;
    }

    @Override
    public List<ResponseGetAllUserReviewDTO> getAllUserReviews(int userId) {
        userService.getUserById(userId);
        log.info("User with ID {} retrieved successfully from the database.", userId);
        List<Review> reviews = reviewRepository.findByUserId(userId);
        log.info("Retrieved reviews with userID {} from the database.", userId);
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
        log.info("Review with ID {} has been successfully saved to the database.", savedReview.getId());
        return reviewMapper.reviewToResponseAddReviewDTO(savedReview);
    }

    @Override
    public ResponseUpdateReviewDTO updateReview(int id, RequestUpdateReviewDTO request) {
        Review review = getReviewById(id);
        userService.getUserById(request.getUserId());
        offerService.getOfferById(request.getOfferId());
        boolean result = transactionService.checkIfUserPurchasedOffer(request.getUserId(), request.getOfferId());
        ReviewBusinessRules.assertIfUserPurchasedOffer(result);
        reviewMapper.updateReviewFromRequestUpdateReviewDto(request, review);
        Review updatedReview = reviewRepository.save(review);
        log.info("Review with ID {} has been successfully updated and saved to the database.", updatedReview.getId());
        return reviewMapper.reviewToResponseUpdateReviewDTO(updatedReview);
    }

    @Override
    public ResponseGetReviewDTO getReview(int id) {
        Review review = getReviewById(id);
        return reviewMapper.reviewToResponseGetReviewDto(review);
    }

    @Override
    public Review getReviewById(int id) {
        Review review = reviewRepository.findById(id).orElseThrow(()->{
            log.warn("Review with ID {} not found in the database.", id);
            return new BusinessException("Review can't be null");
        });
        log.info("Review with ID {} successfully retrieved from the database.", id);
        return review;
    }
}
