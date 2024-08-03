package com.turkcell.staj.dtos.transaction.requests;

import com.turkcell.staj.core.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    // TODO: Package active checking rule
    @NotNull(message = "Package can't be null")
    private int packageId;
    // TODO: User Balance checking rule
    @NotNull(message = "User can't be null")
    private int userId;
    @NotNull(message = "Price can't be null")
    @PositiveOrZero(message = "Price can't be negative")
    private Double price;
    // TODO: Valid status checking rule
    @NotNull(message = "Status can't be null")
    private Status status;
    // TODO: current date checking rule
    private LocalDate createdDate;
}
