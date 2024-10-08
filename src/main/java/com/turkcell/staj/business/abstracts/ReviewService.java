package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;
import com.turkcell.staj.dtos.review.requests.RequestAddReviewDTO;
import com.turkcell.staj.dtos.review.requests.RequestUpdateReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseAddReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetAllUserReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseGetReviewDTO;
import com.turkcell.staj.dtos.review.responses.ResponseUpdateReviewDTO;
import com.turkcell.staj.entities.Review;

import java.util.List;

public interface ReviewService {
    GetOfferReviewsWrapper getAllOfferReviews(int offerId);
    List<ResponseGetAllUserReviewDTO> getAllUserReviews(int userId);
    ResponseAddReviewDTO addReview(RequestAddReviewDTO request);
    ResponseUpdateReviewDTO updateReview(int id, RequestUpdateReviewDTO request);
    ResponseGetReviewDTO getReview(int id);
    Review getReviewById(int id);
}
