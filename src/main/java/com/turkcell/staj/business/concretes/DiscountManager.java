package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.DiscountService;
import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.discounts.requests.RequestAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.requests.RequestUpdateDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetOfferDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseUpdateDiscountDTO;
import com.turkcell.staj.entities.Discount;
import com.turkcell.staj.mappers.DiscountMapper;
import com.turkcell.staj.repositories.DiscountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountManager implements DiscountService {

    private final DiscountRepository discountRepository;
    private final OfferService offerService;
    private final DiscountMapper discountMapper;

    @Override
    public ResponseAddDiscountDTO addDiscount(RequestAddDiscountDTO requestAddDiscountDTO) {
        offerService.getOfferById(requestAddDiscountDTO.getOfferId());
        Discount discount = discountMapper.requestAddDiscountDtoToDiscount(requestAddDiscountDTO);
        Discount savedDiscount = discountRepository.save(discount);
        log.info("Discount with ID {} has been successfully saved to the database.", savedDiscount.getId());
        return discountMapper.discountToResponseAddDiscountDto(savedDiscount);
    }

    @Override
    public ResponseUpdateDiscountDTO updateDiscount(int id, RequestUpdateDiscountDTO requestUpdateDiscountDTO) {
        Discount discount = getDiscountById(id);
        discountMapper.updateDiscountFromRequestUpdateDiscountDto(requestUpdateDiscountDTO, discount);
        offerService.getOfferById(discount.getOffer().getId());
        Discount updatedDiscount = discountRepository.save(discount);
        log.info("Discount with ID {} has been successfully updated to the database.", updatedDiscount.getId());
        return discountMapper.discountToResponseUpdateDiscountDto(updatedDiscount);

    }

    @Override
    public ResponseGetDiscountDTO getDiscount(int id) {
        Discount discount = getDiscountById(id);
        return discountMapper.discountToResponseGetDiscountDto(discount);
    }

    @Override
    public ResponseGetOfferDiscountDTO getOfferDiscount(int offerId) {
        Discount discount = getDiscountByOfferId(offerId);
        return discountMapper.discountToResponseGetOfferDiscountDto(discount);
    }

    @Override
    public Discount getDiscountById(int id) {
        Discount discount = discountRepository.findById(id).orElseThrow(()->{
            log.warn("Discount with ID {} not found in the database.", id);
            return new BusinessException("Discount can't be null.");
        });
        log.info("Discount with ID {} successfully retrieved from database.", id);
        return discount;
    }

    @Override
    public Discount getDiscountByOfferId(int offerId) {
        Discount discount = discountRepository.findByOfferId(offerId).orElseThrow(()->{
            log.warn("Discount with offerID {} not found in the database.", offerId);
            return new BusinessException("Offer discount can't be null.");
        });
        log.info("Offer discount with ID {} successfully retrieved from database.", offerId);
        return discount;

    }
}
