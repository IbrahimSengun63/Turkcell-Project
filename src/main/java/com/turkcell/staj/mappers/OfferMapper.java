package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.entities.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    Offer requestAddOfferDtoToOffer(RequestAddOfferDTO requestAddOfferDTO);

    @Mapping(source = "id",target = "offerId")
    ResponseAddOfferDTO offerToResponseAddOfferDto(Offer offer);
}
