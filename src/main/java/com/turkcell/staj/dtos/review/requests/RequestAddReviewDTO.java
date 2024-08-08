package com.turkcell.staj.dtos.review.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddReviewDTO {
    @NotNull(message = "Offer can't be null")
    @Min(value = 1, message = "Id must be a positive integer greater than or equal to 1")
    private int offerId;
    @NotNull(message = "User can't be null")
    @Min(value = 1, message = "Id must be a positive integer greater than or equal to 1")
    private int userId;
    @Min(value = 1, message = "Rating must be in the range {1, 5}")
    @Max(value = 5, message = "Rating must be in the range {1, 5}")
    private int rating;
    @Size(min = 1, max = 255, message = "Comment length must be in the range {1,255}")
    private String comment;
    // TODO: current date checking rule
    private LocalDate createdDate;
}
