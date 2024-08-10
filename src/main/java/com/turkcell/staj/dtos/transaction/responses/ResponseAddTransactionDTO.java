package com.turkcell.staj.dtos.transaction.responses;

import com.turkcell.staj.core.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAddTransactionDTO {
    private int transactionId;
    private int offerId;
    private int userId;
    private Double price;
    private Status status;
    private LocalDate createdDate;
    private Double userBalanceAfterTransaction;
}
