package com.turkcell.staj.dtos.transaction.requests;

import com.turkcell.staj.core.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class RequestUpdateTransactionDTO {
    @NotNull(message = "Transaction can't be null")
    @Size(min = 1, message = "Id must be positive integer {1,...}")
    private int transactionId;
    @NotNull(message = "Package can't be null")
    @Size(min = 1, message = "Id must be positive integer {1,...}")
    private int packageId;
    // TODO: User Balance checking rule according to the action in the status
    @NotNull(message = "User can't be null")
    @Size(min = 1, message = "Id must be positive integer {1,...}")
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
