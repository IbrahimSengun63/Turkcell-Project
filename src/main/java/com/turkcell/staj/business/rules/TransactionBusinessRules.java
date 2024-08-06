package com.turkcell.staj.business.rules;

import com.turkcell.staj.core.enums.Status;
import com.turkcell.staj.core.exceptions.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public final class TransactionBusinessRules {

    public static void checkIfOfferIsActive(boolean status) {
        if (!status) {
            throw new BusinessException("Inactive offer can not be purchased");
        }
    }

    public static void checkIfUserHasEnoughBalance(double userBalance, double price) {
        if (userBalance < price) {
            throw new BusinessException("User does not have enough balance for this transaction.");
        }
    }

    public static void checkIfStatusCompleted(Status status) {
        if (!status.equals(Status.COMPLETED)) {
            throw new BusinessException("For a new transaction status must be COMPLETED");
        }
    }

    public static void checkIfDateIsCorrect(LocalDate date) {
        if (!LocalDate.now().isEqual(date)) {
            throw new BusinessException("Transaction date must be now");
        }
    }

    public static void checkIfStatusCanceledOrRejected(Status status) {
        if (!(status.equals(Status.CANCELED) || status.equals(Status.REJECTED))) {
            throw new BusinessException("To update a transaction status must be CANCELED or REJECTED");
        }
    }

}
