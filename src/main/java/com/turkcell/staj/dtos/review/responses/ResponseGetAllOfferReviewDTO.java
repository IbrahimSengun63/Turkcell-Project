package com.turkcell.staj.dtos.review.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGetAllOfferReviewDTO {
    private int reviewId;
    private int offerId;
    private int userId;
    private int rating;
    private String comment;
    private LocalDate createdDate;
}
