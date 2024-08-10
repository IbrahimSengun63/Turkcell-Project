package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.business.rules.OfferBusinessRules;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestUpdateOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseUpdateOfferDTO;
import com.turkcell.staj.entities.Offer;
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
    public ResponseAddOfferDTO addOffer(RequestAddOfferDTO requestAddOfferDTO) {
        Offer offer = offerMapper.requestAddOfferDtoToOffer(requestAddOfferDTO);
        Offer savedOffer = offerRepository.save(offer);
        return offerMapper.offerToResponseAddOfferDto(savedOffer);
    }

    @Override
    public ResponseUpdateOfferDTO updateOffer(int id, RequestUpdateOfferDTO requestUpdateOfferDTO) {
        Offer offer = getOfferById(id);
        offerMapper.offerFromRequestUpdateOfferDto(requestUpdateOfferDTO,offer);
        Offer updatedOffer = offerRepository.save(offer);
        return offerMapper.offerToResponseUpdateOfferDto(updatedOffer);
    }

    @Override
    public void deleteOffer(int id) {
        Offer offer = getOfferById(id);
        OfferBusinessRules.checkIfOfferDeleted(offer.getStatus());
        offer.setStatus(false);
        offerRepository.save(offer);
    }

    @Override
    public Offer getOfferById(int id) {
        return offerRepository.findById(id).orElseThrow(() -> new BusinessException("Offer can't be null"));
    }

    @Override
    public List<GetAllResponseOfferDTO> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offerMapper.offersToGetAllResponseOfferDto(offers);
    }

    @Override
    public GetResponseOfferDTO getOffer(int id) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new BusinessException("Offer can't be null"));
        return offerMapper.offerToGetResponseOfferDto(offer);
    }
}
