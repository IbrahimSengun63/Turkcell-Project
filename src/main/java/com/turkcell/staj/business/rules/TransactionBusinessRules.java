package com.turkcell.staj.business.rules;

import com.turkcell.staj.core.enums.Status;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.entities.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public final class TransactionBusinessRules {

    public static void checkIfOfferIsPurchasable(boolean status) {
        if (!status) {
            log.error("Inactive offer can not be purchased");
            throw new BusinessException("Inactive offer can not be purchased");
        }
    }

    public static void checkIfUserHasEnoughBalance(double userBalance, double price) {
        if (userBalance < price) {
            log.error("User does not have enough balance for this transaction.");
            throw new BusinessException("User does not have enough balance for this transaction.");
        }
    }

    public static void checkIfStatusCompleted(Status status) {
        if (!status.equals(Status.COMPLETED)) {
            log.error("For a new transaction status must be COMPLETED");
            throw new BusinessException("For a new transaction status must be COMPLETED");
        }
    }

    public static void checkIfDateIsCorrect(LocalDate date) {
        if (!LocalDate.now().isEqual(date)) {
            log.error("Transaction date must be now");
            throw new BusinessException("Transaction date must be now");
        }
    }

    public static void checkIfStatusCanceledOrRejected(Status status) {
        if (!(status.equals(Status.CANCELED) || status.equals(Status.REJECTED))) {
            log.error("To update a transaction status must be CANCELED or REJECTED");
            throw new BusinessException("To update a transaction status must be CANCELED or REJECTED");
        }
    }

    public static double updateBalanceIfTransactionStatusChangedFromCompleted(Status status, double userBalance, double price) {
        return status.equals(Status.COMPLETED) ? userBalance + price : userBalance;
    }

    public static double calculateUserTotalPurchase(List<Transaction> transactionList) {
        return transactionList.stream()
                .filter(transaction -> transaction.getStatus() == Status.COMPLETED)
                .mapToDouble(Transaction::getPrice)
                .sum();
    }

    public static void checkIfTransactionBelongsToUser(int transactionUserId, int userId){
        if (transactionUserId != userId){
            log.error("Transaction does not belong to this user.");
            throw new BusinessException("Transaction does not belong to this user.");
        }
    }

    public static void checkIfReturnStatusCompleted(Status status) {
        if (!status.equals(Status.COMPLETED)) {
            log.error("To return a transaction status must be COMPLETED");
            throw new BusinessException("To return a transaction status must be COMPLETED");
        }
    }

}
