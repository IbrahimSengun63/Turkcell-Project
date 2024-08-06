package com.turkcell.staj.dtos.transaction.requests;

import com.turkcell.staj.core.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateTransactionDTO {
    private int transactionId;
    @Min(value = 1, message = "Id must be a positive integer greater than or equal to 1")
    private int offerId;
    @Min(value = 1, message = "Id must be a positive integer greater than or equal to 1")
    private int userId;
    @PositiveOrZero(message = "Price can't be negative")
    private double price;
    @NotNull(message = "Status can't be null")
    private Status status;
    private LocalDate createdDate;
}
