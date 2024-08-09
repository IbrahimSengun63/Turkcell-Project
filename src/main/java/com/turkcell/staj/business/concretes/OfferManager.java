package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;
import com.turkcell.staj.mappers.OfferMapper;
import com.turkcell.staj.repositories.OfferRepository;
import com.turkcell.staj.entities.Offer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferManager implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    @Override
    public List<GetAllResponseOfferDTO> getAllOffers() {
        List<Offer> offers = this.offerRepository.findAll();
        return this.offerMapper.offersToGetAllResponseOfferDto(offers);
    }

    @Override
    public GetResponseOfferDTO getOffer(int id) {
        Offer offer = this.offerRepository.findById(id).orElseThrow(() -> new BusinessException("Offer can't be null"));
        return this.offerMapper.offerToGetResponseOfferDto(offer);
    }
}
