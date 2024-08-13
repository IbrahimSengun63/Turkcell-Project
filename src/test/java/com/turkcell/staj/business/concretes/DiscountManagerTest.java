package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.discounts.requests.RequestAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.requests.RequestUpdateDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseAddDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseGetOfferDiscountDTO;
import com.turkcell.staj.dtos.discounts.responses.ResponseUpdateDiscountDTO;
import com.turkcell.staj.entities.Discount;
import com.turkcell.staj.entities.Offer;
import com.turkcell.staj.mappers.DiscountMapper;
import com.turkcell.staj.repositories.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscountManagerTest {

    @InjectMocks
    private DiscountManager discountManager;
    @Mock
    private DiscountRepository discountRepository;
    @Mock
    private OfferService offerService;
    @Mock
    private DiscountMapper discountMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldAddDiscount() {
        // Do
        int offerId = 2;
        int discountId = 3;
        int discountRate = 50;
        RequestAddDiscountDTO request = new RequestAddDiscountDTO(discountRate, offerId, true);
        Offer offer = new Offer(1, "offername1", "desc1", 35.12, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        Discount discount = new Discount(offerId, discountRate, true, offer);
        Discount savedDiscount = new Discount(offerId, discountRate, true, offer);
        ResponseAddDiscountDTO response = new ResponseAddDiscountDTO(discountId, offerId, discountRate, true);
        // When
        when(offerService.getOfferById(request.getOfferId())).thenReturn(offer);
        when(discountMapper.requestAddDiscountDtoToDiscount(request)).thenReturn(discount);
        when(discountRepository.save(discount)).thenReturn(savedDiscount);
        when(discountMapper.discountToResponseAddDiscountDto(savedDiscount)).thenReturn(response);
        // Act
        ResponseAddDiscountDTO result = discountManager.addDiscount(request);
        // Assert
        assertNotNull(result);
        assertEquals(request.getDiscountRate(), result.getDiscountRate());
        assertEquals(request.getStatus(), result.getStatus());
        assertEquals(request.getOfferId(), result.getOfferId());
        // Verify
        verify(offerService, times(1)).getOfferById(request.getOfferId());
        verify(discountMapper, times(1)).requestAddDiscountDtoToDiscount(request);
        verify(discountRepository, times(1)).save(discount);
        verify(discountMapper, times(1)).discountToResponseAddDiscountDto(savedDiscount);

    }

    @Test
    public void shouldUpdateDiscount() {
        // Do
        int offerId = 2;
        int discountId = 3;
        int discountRate = 50;
        Offer offer = new Offer(1, "offername1", "desc1", 35.12, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        Discount discount = new Discount(offerId, discountRate, true, offer);
        Discount updatedDiscount = new Discount(offerId, discountRate, true, offer);
        RequestUpdateDiscountDTO request = new RequestUpdateDiscountDTO(discountRate, true);
        ResponseUpdateDiscountDTO response = new ResponseUpdateDiscountDTO(discountId, 2, discountRate, true);
        // When
        when(discountRepository.findById(discountId)).thenReturn(Optional.of(discount));
        when(offerService.getOfferById(discount.getOffer().getId())).thenReturn(offer);
        when(discountRepository.save(discount)).thenReturn(updatedDiscount);
        when(discountMapper.discountToResponseUpdateDiscountDto(updatedDiscount)).thenReturn(response);
        // Act
        ResponseUpdateDiscountDTO result = discountManager.updateDiscount(discountId, request);
        // Assert
        assertNotNull(result);
        assertEquals(request.getDiscountRate(), result.getDiscountRate());
        assertEquals(request.getStatus(), result.getStatus());
        // Verify
        verify(discountRepository, times(1)).findById(discountId);
        verify(offerService, times(1)).getOfferById(discount.getOffer().getId());
        verify(discountRepository, times(1)).save(discount);
        verify(discountMapper, times(1)).discountToResponseUpdateDiscountDto(updatedDiscount);
    }

    @Test
    public void shouldGetDiscount() {
        // Do
        int discountId = 3;
        int offerId = 2;
        int discountRate = 50;
        Offer offer = new Offer(1, "offername1", "desc1", 35.12, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        Discount discount = new Discount(offerId, discountRate, true, offer);
        ResponseGetDiscountDTO response = new ResponseGetDiscountDTO(discountId, offerId, discountRate, true);
        // When
        when(discountRepository.findById(discountId)).thenReturn(Optional.of(discount));
        when(discountMapper.discountToResponseGetDiscountDto(discount)).thenReturn(response);
        // Act
        ResponseGetDiscountDTO result = discountManager.getDiscount(discountId);
        // Assert
        assertNotNull(result);
        assertEquals(discountId, result.getDiscountId());
        // Verify
        verify(discountRepository, times(1)).findById(discountId);
        verify(discountMapper, times(1)).discountToResponseGetDiscountDto(discount);
    }

    @Test
    public void shouldThrowBusinessExceptionWhenDiscountDoesNotExist() {
        // Do
        int id = 3;
        // When
        when(discountRepository.findById(id)).thenReturn(Optional.empty());
        // Act and Assert
        var exp = assertThrows(BusinessException.class, () -> discountManager.getDiscountById(id));
        assertEquals("Discount can't be null.", exp.getMessage());
        // Verify
        verify(discountRepository, times(1)).findById(id);
    }

    @Test
    public void shouldGetOfferDiscount() {
        // Do
        int discountId = 3;
        int offerId = 2;
        int discountRate = 50;
        Offer offer = new Offer(1, "offername1", "desc1", 35.12, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        Discount discount = new Discount(offerId, discountRate, true, offer);
        ResponseGetOfferDiscountDTO response = new ResponseGetOfferDiscountDTO(discountId, offerId, discountRate, true);
        // When
        when(discountRepository.findByOfferId(offerId)).thenReturn(Optional.of(discount));
        when(discountMapper.discountToResponseGetOfferDiscountDto(discount)).thenReturn(response);
        // Act
        ResponseGetOfferDiscountDTO result = discountManager.getOfferDiscount(offerId);
        // Assert
        assertNotNull(result);
        assertEquals(offerId, result.getOfferId());
        // Verify
        verify(discountRepository, times(1)).findByOfferId(offerId);
        verify(discountMapper, times(1)).discountToResponseGetOfferDiscountDto(discount);
    }

    @Test
    public void shouldThrowBusinessExceptionWhenOfferDiscountDoesNotExist() {

        // Do
        int id = 3;
        // When
        when(discountRepository.findByOfferId(id)).thenReturn(Optional.empty());
        // Act and Assert
        var exp = assertThrows(BusinessException.class, () -> discountManager.getDiscountByOfferId(id));
        assertEquals("Offer discount can't be null.", exp.getMessage());
        // Verify
        verify(discountRepository, times(1)).findByOfferId(id);
    }

    @Test
    public void shouldReturnZeroIfOfferDiscountDoesNotExist() {
        // Do
        boolean offerDiscountExists = false;
        Offer offer = new Offer(1, "offername1", "desc1", 35.12, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        // When
        when(discountRepository.existsByOfferId(offer.getId())).thenReturn(offerDiscountExists);
        // Act
        double result = discountManager.getOfferDiscountAmount(offer);
        // Assert
        assertEquals(0, result);
        // Verify
        verify(discountRepository, times(1)).existsByOfferId(offer.getId());
    }

    @Test
    public void shouldReturnZeroIfDiscountIsNotActive() {
        // Do
        int offerId = 2;
        int discountRate = 50;
        boolean offerDiscountExists = true;
        boolean discountStatus = false;
        Offer offer = new Offer(1, "offername1", "desc1", 35.12, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        Discount discount = new Discount(offerId, discountRate, discountStatus, offer);
        // When
        when(discountRepository.existsByOfferId(offer.getId())).thenReturn(offerDiscountExists);
        when(discountRepository.findByOfferId(offer.getId())).thenReturn(Optional.of(discount));
        // Act
        double result = discountManager.getOfferDiscountAmount(offer);
        // Assert
        assertEquals(0, result);
        // Verify
        verify(discountRepository, times(1)).existsByOfferId(offer.getId());
        verify(discountRepository, times(1)).findByOfferId(offer.getId());
    }

    @Test
    public void shouldGetOfferDiscountAmount() {
        // Do
        int offerId = 2;
        int discountRate = 50;
        boolean offerDiscountExists = true;
        boolean discountStatus = true;
        Offer offer = new Offer(1, "offername1", "desc1", 35.12, true, new ArrayList<>(), new ArrayList<>(), new Discount());
        Discount discount = new Discount(offerId, discountRate, discountStatus, offer);
        // When
        when(discountRepository.existsByOfferId(offer.getId())).thenReturn(offerDiscountExists);
        when(discountRepository.findByOfferId(offer.getId())).thenReturn(Optional.of(discount));
        // Act
        double result = discountManager.getOfferDiscountAmount(offer);
        // Assert
        assertEquals(offer.getPrice() * discountRate / 100.0, result);
        // Verify
        verify(discountRepository, times(1)).existsByOfferId(offer.getId());
        verify(discountRepository, times(1)).findByOfferId(offer.getId());
    }
}