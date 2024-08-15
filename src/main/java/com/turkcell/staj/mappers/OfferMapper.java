package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestUpdateOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseUpdateOfferDTO;
import com.turkcell.staj.entities.Offer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    Offer requestAddOfferDtoToOffer(RequestAddOfferDTO requestAddOfferDTO);

    @Mapping(source = "id", target = "offerId")
    ResponseAddOfferDTO offerToResponseAddOfferDto(Offer offer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void offerFromRequestUpdateOfferDto(RequestUpdateOfferDTO requestUpdateOfferDTO, @MappingTarget Offer offer);

    @Mapping(source = "id", target = "offerId")
    ResponseUpdateOfferDTO offerToResponseUpdateOfferDto(Offer offer);

    @Mapping(source = "id", target = "offerId")
    GetAllResponseOfferDTO offerToGetAllResponseOfferDto(Offer offer);

    List<GetAllResponseOfferDTO> offersToGetAllResponseOfferDto(List<Offer> offers);

    @Mapping(source = "id", target = "offerId")
    GetResponseOfferDTO offerToGetResponseOfferDto(Offer offer);
}
