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
public class ResponseGetAllUserTransactionDTO {
    private int transactionId;
    private int userId;
    private int offerId;
    private double price;
    private Status status;
    private LocalDate createdDate;
}
