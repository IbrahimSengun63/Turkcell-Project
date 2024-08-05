package com.turkcell.staj.controllers.responseWrappers;

import com.turkcell.staj.dtos.review.responses.ResponseGetAllOfferReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetOfferReviewsWrapper {
    private List<ResponseGetAllOfferReviewDTO> offerReviews;
    private double offerAvgRating;
}
