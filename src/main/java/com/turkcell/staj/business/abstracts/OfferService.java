package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestUpdateOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseUpdateOfferDTO;
import com.turkcell.staj.entities.Offer;

public interface OfferService {
    ResponseAddOfferDTO addOffer(RequestAddOfferDTO requestAddOfferDTO);
    ResponseUpdateOfferDTO updateOffer(int id, RequestUpdateOfferDTO requestUpdateOfferDTO);
    Offer getOfferById(int id);
}
