package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;

public interface TransactionService {
    ResponseAddTransactionDTO addTransaction(RequestAddTransactionDTO requestAddTransactionDTO);
}
