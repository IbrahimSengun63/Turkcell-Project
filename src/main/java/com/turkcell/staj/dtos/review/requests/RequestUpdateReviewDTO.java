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
public class RequestUpdateReviewDTO {
    private Integer rating;
    @Size(min = 3, max = 255, message = "Comment length must be in the range {3,255}")
    private String comment;
    private LocalDate createdDate;
}
