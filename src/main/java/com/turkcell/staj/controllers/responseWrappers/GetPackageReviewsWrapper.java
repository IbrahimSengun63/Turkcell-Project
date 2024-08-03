package com.turkcell.staj.controllers.responseWrappers;

import com.turkcell.staj.dtos.review.responses.ResponseGetAllPackageReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPackageReviewsWrapper {
    private List<ResponseGetAllPackageReviewDTO> packageReviews;
    private double packageAvgRating;
}
