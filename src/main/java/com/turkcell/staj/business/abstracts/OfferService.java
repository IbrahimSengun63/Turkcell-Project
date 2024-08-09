package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestUpdateOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseUpdateOfferDTO;
import com.turkcell.staj.entities.Offer;
import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;

import java.util.List;

public interface OfferService {
    ResponseAddOfferDTO addOffer(RequestAddOfferDTO requestAddOfferDTO);
    ResponseUpdateOfferDTO updateOffer(int id, RequestUpdateOfferDTO requestUpdateOfferDTO);
    void deleteOffer(int id);
    Offer getOfferById(int id);
    List<GetAllResponseOfferDTO> getAllOffers();
    GetResponseOfferDTO getOffer(int id);
}
