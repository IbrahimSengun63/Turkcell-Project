package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestUpdateOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseUpdateOfferDTO;
import com.turkcell.staj.entities.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    Offer requestAddOfferDtoToOffer(RequestAddOfferDTO requestAddOfferDTO);

    @Mapping(source = "id", target = "offerId")
    ResponseAddOfferDTO offerToResponseAddOfferDto(Offer offer);

    @Mapping(source = "offerId", target = "id")
    Offer requestUpdateOfferDtoToOffer(RequestUpdateOfferDTO requestUpdateOfferDTO);

    @Mapping(source = "id", target = "offerId")
    ResponseUpdateOfferDTO offerToResponseUpdateOfferDto(Offer offer);
}
