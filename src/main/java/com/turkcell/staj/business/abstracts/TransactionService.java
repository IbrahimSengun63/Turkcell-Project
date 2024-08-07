package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.controllers.responseWrappers.GetUserTransactionsWrapper;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.requests.RequestUpdateTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseUpdateTransactionDTO;

public interface TransactionService {
    ResponseAddTransactionDTO addTransaction(RequestAddTransactionDTO requestAddTransactionDTO);
    ResponseUpdateTransactionDTO updateTransaction(RequestUpdateTransactionDTO requestUpdateTransactionDTO);
    GetUserTransactionsWrapper getHistory(int userId);
}
