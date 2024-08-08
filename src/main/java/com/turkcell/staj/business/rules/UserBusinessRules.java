package com.turkcell.staj.business.rules;

import com.turkcell.staj.core.exceptions.BusinessException;
import org.springframework.stereotype.Service;

@Service
public final class UserBusinessRules {

    public static void checkIfUserNameIsValid(String name) {
        if (name == null || name.trim().isEmpty() || name.length() < 3) {
            throw new BusinessException("User name must be at least 3 characters long and can't be blank.");
        }
    }

    public static void checkIfUserSurnameIsValid(String surname) {
        if (surname == null || surname.trim().isEmpty() || surname.length() < 3) {
            throw new BusinessException("User surname must be at least 3 characters long and can't be blank.");
        }
    }

    public static void checkIfUserBalanceIsPositive(double balance) {
        if (balance < 0) {
            throw new BusinessException("User balance must be zero or positive.");
        }
    }

    public static void checkIfUserExists(Object user) {
        if (user == null) {
            throw new BusinessException("User does not exist.");
        }
    }

    public static void checkIfUserHasSufficientBalance(double userBalance, double requiredAmount) {
        if (userBalance < requiredAmount) {
            throw new BusinessException("User does not have enough balance for this operation.");
        }
    }

    public static void checkIfUserBalanceIsSufficientForTransaction(double userBalance, double transactionAmount) {
        if (userBalance < transactionAmount) {
            throw new BusinessException("User does not have enough balance to complete the transaction.");
        }
    }

    public static void checkIfUserIsActive(boolean isActive) {
        if (!isActive) {
            throw new BusinessException("Inactive user can't perform this operation.");
        }
    }

}

