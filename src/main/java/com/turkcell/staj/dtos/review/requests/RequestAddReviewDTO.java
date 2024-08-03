package com.turkcell.staj.dtos.review.requests;

import jakarta.validation.constraints.NotBlank;
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
    @NotNull(message = "Package can't be null")
    @Size(min = 1, message = "Id must be positive integer {1,...}")
    private int packageId;
    @NotNull(message = "User can't be null")
    @Size(min = 1, message = "Id must be positive integer {1,...}")
    private int userId;
    @Size(min = 1, max = 5, message = "Rating must be {1,5} range")
    private int rating;
    private String comment;
    // TODO: current date checking rule
    private LocalDate createdDate;
}
