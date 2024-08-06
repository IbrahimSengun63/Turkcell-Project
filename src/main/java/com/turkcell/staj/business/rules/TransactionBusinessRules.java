package com.turkcell.staj.business.rules;

import com.turkcell.staj.core.exceptions.BusinessException;
import org.springframework.stereotype.Service;

@Service
public final class TransactionBusinessRules {
    // Private constructor to prevent instantiation
    private TransactionBusinessRules() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void checkIfUserHasEnoughBalance(double userBalance, double price) {
        if (userBalance < price) {
            throw new BusinessException("User does not have enough balance for this transaction.");
        }
    }

}
