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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferManager implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @Override
    public ResponseAddOfferDTO addOffer(RequestAddOfferDTO requestAddOfferDTO) {
        Offer offer = offerMapper.requestAddOfferDtoToOffer(requestAddOfferDTO);
        Offer savedOffer = offerRepository.save(offer);
        log.info("Offer with ID {} has been successfully saved to the database.", savedOffer.getId());
        return offerMapper.offerToResponseAddOfferDto(savedOffer);
    }

    @Override
    public ResponseUpdateOfferDTO updateOffer(int id, RequestUpdateOfferDTO requestUpdateOfferDTO) {
        Offer offer = getOfferById(id);
        offerMapper.offerFromRequestUpdateOfferDto(requestUpdateOfferDTO, offer);
        Offer updatedOffer = offerRepository.save(offer);
        log.info("Offer with ID {} has been successfully updated and saved to the database.", id);
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
        Offer offer = offerRepository.findById(id).orElseThrow(() -> {
            log.warn("Offer with ID {} not found in the database.", id);
            return new BusinessException("Offer can't be null");
        });
        log.info("Offer with ID {} successfully retrieved from the database.", id);
        return offer;
    }

    @Override
    public List<GetAllResponseOfferDTO> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        log.info("Retrieved all offers from the database.");
        return offerMapper.offersToGetAllResponseOfferDto(offers);
    }

    @Override
    public GetResponseOfferDTO getOffer(int id) {
        Offer offer = getOfferById(id);
        return offerMapper.offerToGetResponseOfferDto(offer);
    }
}
