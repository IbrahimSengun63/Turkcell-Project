package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.dtos.discounts.requests.RequestAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.requests.RequestUpdateDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetOfferDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseUpdateDiscountDTO;
import com.turkcell.staj.entities.Discount;
import com.turkcell.staj.entities.Offer;

public interface DiscountService {
    ResponseAddDiscountDTO addDiscount(RequestAddDiscountDTO requestAddDiscountDTO);
    ResponseUpdateDiscountDTO updateDiscount(int id, RequestUpdateDiscountDTO requestUpdateDiscountDTO);
    ResponseGetDiscountDTO getDiscount(int id);
    ResponseGetOfferDiscountDTO getOfferDiscount(int offerId);
    Discount getDiscountById(int id);
    Discount getDiscountByOfferId(int offerId);
    boolean checkIfOfferDiscountExists(int offerId);
    double getOfferDiscountAmount(Offer offer);
}
