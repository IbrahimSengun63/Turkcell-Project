package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.controllers.responseWrappers.GetOfferReviewsWrapper;

public interface ReviewService {
    GetOfferReviewsWrapper getAllOfferReviews(int offerId);
}
