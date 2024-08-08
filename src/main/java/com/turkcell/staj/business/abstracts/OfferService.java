package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;

public interface OfferService {
    ResponseAddOfferDTO addOffer(RequestAddOfferDTO requestAddOfferDTO);
}
