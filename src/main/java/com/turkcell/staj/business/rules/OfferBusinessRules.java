package com.turkcell.staj.business.rules;

import com.turkcell.staj.core.exceptions.BusinessException;
import org.springframework.stereotype.Service;

@Service
public final class OfferBusinessRules {

    public static void checkIfOfferDeleted(boolean status) {
        if (!status) {
            throw new BusinessException("Offer is already deleted.");
        }
    }
}
