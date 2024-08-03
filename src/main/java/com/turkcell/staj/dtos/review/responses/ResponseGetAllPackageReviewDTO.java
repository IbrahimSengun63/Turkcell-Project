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
public class ResponseGetAllPackageReviewDTO {
    private int reviewId;
    private int packageId;
    private int userId;
    private int rating;
    private String comment;
    private LocalDate createdDate;
}
