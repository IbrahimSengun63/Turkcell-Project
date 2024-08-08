package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.entities.Offer;
import com.turkcell.staj.mappers.OfferMapper;
import com.turkcell.staj.repositories.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferManager implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @Override
    public ResponseAddOfferDTO addOffer(RequestAddOfferDTO requestAddOfferDTO) {
        Offer offer = this.offerMapper.requestAddOfferDtoToOffer(requestAddOfferDTO);
        Offer savedOffer = this.offerRepository.save(offer);
        return this.offerMapper.offerToResponseAddOfferDto(savedOffer);
    }
}
