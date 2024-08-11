package com.turkcell.staj.dtos.transaction.requests;

import com.turkcell.staj.core.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateTransactionDTO {
    private int offerId;
    @Min(value = 1, message = "Id must be a positive integer greater than or equal to 1")
    private int userId;
    @NotNull(message = "Status can't be null")
    private Status status;
}
