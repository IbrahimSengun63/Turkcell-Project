package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.rules.OfferBusinessRules;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.offers.requests.RequestAddOfferDTO;
import com.turkcell.staj.dtos.offers.requests.RequestUpdateOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetAllResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.GetResponseOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseAddOfferDTO;
import com.turkcell.staj.dtos.offers.responses.ResponseUpdateOfferDTO;
import com.turkcell.staj.entities.Discount;
import com.turkcell.staj.entities.Offer;
import com.turkcell.staj.entities.Review;
import com.turkcell.staj.entities.Transaction;
import com.turkcell.staj.mappers.OfferMapper;
import com.turkcell.staj.repositories.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OfferManagerTest {

    @InjectMocks
    private OfferManager offerManager;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private OfferMapper offerMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldAddOffer() {
        // Do
        RequestAddOfferDTO request = new RequestAddOfferDTO("offername1", "desc1", 35D, true);
        Offer offer = new Offer(1,"offername1","desc1",35D, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        ResponseAddOfferDTO response = new ResponseAddOfferDTO(1, "offername1", "desc1", 35D, true);

        // When
        when(offerMapper.requestAddOfferDtoToOffer(request)).thenReturn(offer);
        when(offerRepository.save(offer)).thenReturn(offer);
        when(offerMapper.offerToResponseAddOfferDto(offer)).thenReturn(response);

        // Act
        ResponseAddOfferDTO result = offerManager.addOffer(request);

        // Assert
        assertNotNull(result);
        assertEquals(request.getOfferName(), result.getOfferName());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getStatus(), result.getStatus());
        assertEquals(request.getPrice(), result.getPrice());
        assertEquals(offer.getId(), result.getOfferId());

        //Verify
        verify(offerMapper, times(1)).requestAddOfferDtoToOffer(request);
        verify(offerRepository, times(1)).save(offer);
        verify(offerMapper, times(1)).offerToResponseAddOfferDto(offer);

    }

    @Test
    public void shouldUpdateOffer() {
        // Do
        int id = 1;
        Offer offer = new Offer(id,"offername1","desc1",35.23, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        Offer updatedOffer = new Offer(id,"offernameupdated","descupdated",45.65, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        RequestUpdateOfferDTO request = new RequestUpdateOfferDTO("offernameupdated","descupdated",45D, true);
        ResponseUpdateOfferDTO response = new ResponseUpdateOfferDTO(1, "offernameupdated","descupdated",45D, true);
        // When
        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
        when(offerRepository.save(offer)).thenReturn(updatedOffer);
        when(offerMapper.offerToResponseUpdateOfferDto(updatedOffer)).thenReturn(response);
        // Act
        ResponseUpdateOfferDTO result =  offerManager.updateOffer(id, request);
        // Assert
        assertNotNull(result);
        assertEquals(id, result.getOfferId());
        assertEquals(request.getOfferName(), result.getOfferName());
        assertEquals(request.getStatus(), result.getStatus());
        assertEquals(request.getPrice(), result.getPrice());
        assertEquals(request.getDescription(), result.getDescription());
        //Verify
        verify(offerRepository, times(1)).findById(id);
        verify(offerRepository, times(1)).save(offer);
        verify(offerMapper, times(1)).offerToResponseUpdateOfferDto(updatedOffer);

    }

    @Test
    public void shouldThrowBusinessExceptionWhenOfferDoesNotExist() {
        // Do
        int id = 3;
        // When
        when(offerRepository.findById(id)).thenReturn(Optional.empty());
        // Act and Assert
        var exp = assertThrows(BusinessException.class, () -> offerManager.getOfferById(id));
        assertEquals("Offer can't be null", exp.getMessage());
        // Verify
        verify(offerRepository, times(1)).findById(id);
    }

    @Test
    public void shouldDeleteOffer() {
        // Do
        int id = 3;
        Offer offer = new Offer(id,"offername1","desc1",35.23, true, new ArrayList<>(), new ArrayList<>(), new Discount());

        // When
        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
        // Act
        offerManager.deleteOffer(id);
        // Assert ?
        // TODO
        // Verify
        verify(offerRepository, times(1)).findById(id);
    }

    @Test
    public void shouldGetOffer(){
        // Do
        int id = 3;
        Offer offer = new Offer(id,"offername1","desc1",35.23, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        GetResponseOfferDTO response = new GetResponseOfferDTO(id, "offername1","desc1",35.23, true);
        // When
        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));
        when(offerMapper.offerToGetResponseOfferDto(offer)).thenReturn(response);
        // Act
        GetResponseOfferDTO result = offerManager.getOffer(id);
        // Assert
        assertNotNull(result);
        assertEquals(response, result);
        // Verify
        verify(offerRepository, times(1)).findById(id);
        verify(offerMapper, times(1)).offerToGetResponseOfferDto(offer);
    }

    @Test
    public void shouldGetAllOffers(){
        // Do
        List<GetAllResponseOfferDTO> dtos = List.of(
                new GetAllResponseOfferDTO(1, "offername1","desc1",35.23, true),
                new GetAllResponseOfferDTO(2, "offername2","desc2",55.62, false)
        );
        List<Offer> offers = List.of(
                new Offer(1, "offername1","desc1",35.23, true, new ArrayList<>(), new ArrayList<>(), new Discount()),
                new Offer(2, "offername2","desc2",55.62, false, new ArrayList<>(), new ArrayList<>(), new Discount())
        );

        // When
        when(offerRepository.findAll()).thenReturn(offers);
        when(offerMapper.offersToGetAllResponseOfferDto(offers)).thenReturn(dtos);
        // Act
        List<GetAllResponseOfferDTO> result = offerManager.getAllOffers();
        // Assert
        assertNotNull(result);
        assertEquals(dtos, result);
        // Verify
        verify(offerRepository, times(1)).findAll();
        verify(offerMapper, times(1)).offersToGetAllResponseOfferDto(offers);
    }

}