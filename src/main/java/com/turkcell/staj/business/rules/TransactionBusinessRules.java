package com.turkcell.staj.business.rules;

import com.turkcell.staj.core.exceptions.BusinessException;
import org.springframework.stereotype.Service;

@Service
public final class TransactionBusinessRules {
    public static void checkIfUserHasEnoughBalance(double userBalance, double price) {
        if (userBalance < price) {
            throw new BusinessException("User does not have enough balance for this transaction.");
        }
    }

}
