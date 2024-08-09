package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;

import java.util.List;

public interface OfferService {
    List<GetAllResponseOfferDTO> getAllOffers();
    GetResponseOfferDTO getOffer(int id);
}
