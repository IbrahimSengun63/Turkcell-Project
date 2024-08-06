package com.turkcell.staj.dtos.transaction.requests;

import com.turkcell.staj.core.enums.Status;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAddTransactionDTO {
    // TODO: Offer active checking rule
    @Min(value = 1, message = "Id must be a positive integer greater than or equal to 1")
    private int offerId;
    @Min(value = 1, message = "Id must be a positive integer greater than or equal to 1")
    private int userId;
    @PositiveOrZero(message = "Price can't be negative")
    private double price;
    // TODO: Valid status checking rule
    @NotNull(message = "Status can't be null")
    private Status status;
    // TODO: current date checking rule
    private LocalDate createdDate;
}
