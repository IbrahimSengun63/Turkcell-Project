package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllUserReviewDTO;

import java.util.List;

public interface ReviewService {
    GetOfferReviewsWrapper getAllOfferReviews(int offerId);
    List<ResponseGetAllUserReviewDTO> getAllUserReviews(int userId);
    ResponseAddReviewDTO addReview(RequestAddReviewDTO request);

}
