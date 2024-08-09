package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;
import com.turkcell.staj.entities.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    @Mapping(source = "id", target = "offerId")
    GetAllResponseOfferDTO offerToGetAllResponseOfferDto(Offer offer);

    List<GetAllResponseOfferDTO> offersToGetAllResponseOfferDto(List<Offer> offers);

    @Mapping(source = "id", target = "offerId")
    GetResponseOfferDTO offerToGetResponseOfferDto(Offer offer);
}
