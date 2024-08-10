package com.turkcell.staj.business.rules;

import com.turkcell.staj.core.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public final class OfferBusinessRules {

    public static void checkIfOfferDeleted(boolean status) {
        if (!status) {
            log.error("Offer is already deleted.");
            throw new BusinessException("Offer is already deleted.");
        }
    }
}
