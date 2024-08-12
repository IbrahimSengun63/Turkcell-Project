package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.discounts.requests.RequestAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.requests.RequestUpdateDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetOfferDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseUpdateDiscountDTO;
import com.turkcell.staj.entities.Discount;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    DiscountMapper INSTANCE = Mappers.getMapper(DiscountMapper.class);

    @Mapping(source = "offerId", target = "offer.id")
    Discount requestAddDiscountDtoToDiscount(RequestAddDiscountDTO requestAddDiscountDTO);
    @Mapping(source = "id", target = "discountId")
    @Mapping(source = "offer.id", target = "offerId")
    ResponseAddDiscountDTO discountToResponseAddDiscountDto(Discount discount);

    @Mapping(target = "offer.id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDiscountFromRequestUpdateDiscountDto(RequestUpdateDiscountDTO requestUpdateDiscountDTO, @MappingTarget Discount discount);
    @Mapping(source = "id", target = "discountId")
    @Mapping(source = "offer.id", target = "offerId")
    ResponseUpdateDiscountDTO discountToResponseUpdateDiscountDto(Discount discount);

    @Mapping(source = "id", target = "discountId")
    @Mapping(source = "offer.id", target = "offerId")
    ResponseGetDiscountDTO discountToResponseGetDiscountDto(Discount discount);

    @Mapping(source = "id", target = "discountId")
    @Mapping(source = "offer.id", target = "offerId")
    ResponseGetOfferDiscountDTO discountToResponseGetOfferDiscountDto(Discount discount);
}

