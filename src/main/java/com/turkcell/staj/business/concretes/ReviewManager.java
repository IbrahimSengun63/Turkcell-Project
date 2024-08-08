package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.ReviewService;
import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.business.rules.ReviewBusinessRules;
import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.requests.RequestUpdateReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllOfferReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllUserReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseUpdateReviewDTO;
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

    private final TransactionService transactionService;
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

    @Override
    public ResponseAddReviewDTO addReview(RequestAddReviewDTO request) {
        this.userRepository.findById(request.getUserId()).orElseThrow(() -> new BusinessException("User can't be null"));
        this.offerRepository.findById(request.getOfferId()).orElseThrow(() -> new BusinessException("Offer can't be null"));
        boolean result = this.transactionService.checkIfUserPurchasedOffer(request.getUserId(), request.getOfferId());
        ReviewBusinessRules.assertIfUserPurchasedOffer(result);
        Review review = this.reviewMapper.requestAddReviewDtoToReview(request);
        Review savedReview = this.reviewRepository.save(review);
        return this.reviewMapper.reviewToResponseAddReviewDTO(savedReview);
    }

    @Override
    public ResponseUpdateReviewDTO updateReview(int id, RequestUpdateReviewDTO request) {
        // Get review from db and make checks
        Review review = this.reviewRepository.findById(id).orElseThrow(() -> new BusinessException("Review can't be null"));
        this.userRepository.findById(request.getUserId()).orElseThrow(() -> new BusinessException("User can't be null"));
        this.offerRepository.findById(request.getOfferId()).orElseThrow(() -> new BusinessException("Offer can't be null"));
        boolean result = this.transactionService.checkIfUserPurchasedOffer(request.getUserId(), request.getOfferId());
        ReviewBusinessRules.assertIfUserPurchasedOffer(result);
        this.reviewMapper.updateReviewFromRequestUpdateReviewDto(request, review);
        Review savedReview = this.reviewRepository.save(review);
        return this.reviewMapper.reviewToResponseUpdateReviewDTO(savedReview);
    }
}
