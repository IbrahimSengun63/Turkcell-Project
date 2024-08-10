package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.controllers.responseWrappers.GetUserTransactionsWrapper;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.requests.RequestUpdateTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseReturnTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseUpdateTransactionDTO;
import com.turkcell.staj.entities.Transaction;

public interface TransactionService {
    ResponseAddTransactionDTO addTransaction(RequestAddTransactionDTO requestAddTransactionDTO);
    ResponseUpdateTransactionDTO updateTransaction(int id,RequestUpdateTransactionDTO requestUpdateTransactionDTO);
    GetUserTransactionsWrapper getHistory(int userId);
    ResponseReturnTransactionDTO returnTransaction(int transactionId, int userId);
    boolean checkIfUserPurchasedOffer(int userId, int offerId);
    Transaction getTransactionById(int id);
}
