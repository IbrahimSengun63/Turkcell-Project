package com.turkcell.staj.controllers.responseWrappers;

import com.turkcell.staj.dtos.transaction.responses.ResponseGetAllUserTransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapperForGetUserTransactions {
    private List<ResponseGetAllUserTransactionDTO> userTransactions;
    private double totalPurchaseHistory;
}
