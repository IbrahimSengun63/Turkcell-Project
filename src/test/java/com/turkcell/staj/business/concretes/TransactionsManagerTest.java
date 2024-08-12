package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.DiscountService;
import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.business.rules.TransactionBusinessRules;
import com.turkcell.staj.core.enums.Status;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.entities.Offer;
import com.turkcell.staj.entities.Transaction;
import com.turkcell.staj.entities.User;
import com.turkcell.staj.mappers.TransactionMapper;
import com.turkcell.staj.repositories.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionsManagerTest {

    @InjectMocks
    private TransactionsManager transactionsManager;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private OfferService offerService;

    @Mock
    private DiscountService discountService;

    @Mock
    private TransactionMapper transactionMapper;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void shouldAddTransaction() {
        // arrange
        int offerId = 1;
        Boolean offerStatus = true;
        double offerPrice = 100.0;
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setStatus(offerStatus);
        offer.setPrice(offerPrice);

        int userId = 1;
        double userBalance = 90.0;
        User user = new User();
        user.setId(userId);
        user.setBalance(userBalance);

        int transactionId = 1;
        Status status = Status.COMPLETED;
        LocalDate date = LocalDate.now();
        double price = 90.0;
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setUser(user);
        transaction.setOffer(offer);
        transaction.setStatus(status);
        transaction.setCreatedDate(LocalDate.now());
        transaction.setPrice(price);

        double discountAmount = 10.0;
        double userBalanceAfterTransaction = 0.0;

        RequestAddTransactionDTO request = new RequestAddTransactionDTO(offerId, userId, status, date);
        ResponseAddTransactionDTO response = new ResponseAddTransactionDTO(transactionId, offerId, userId, null, price, status, date, null);

        // when
        when(transactionMapper.requestAddTransactionDtoToTransaction(request)).thenReturn(transaction);
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(discountService.getOfferDiscountAmount(offer)).thenReturn(discountAmount);
        when(userService.getUserById(userId)).thenReturn(user);
        doNothing().when(userService).saveUser(user);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.transactionToResponseAddTransactionDto(transaction)).thenReturn(response);

        // act
        ResponseAddTransactionDTO result = transactionsManager.addTransaction(request);

        // assert
        assertNotNull(result);
        assertEquals(response, result);
        assertEquals(offerPrice, result.getOfferPrice());
        assertEquals(userBalanceAfterTransaction, result.getUserBalanceAfterTransaction());

        //verify
        verify(transactionMapper, times(1)).requestAddTransactionDtoToTransaction(request);
        verify(offerService, times(1)).getOfferById(offerId);
        verify(discountService, times(1)).getOfferDiscountAmount(offer);
        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).saveUser(user);
        verify(transactionRepository, times(1)).save(transaction);
        verify(transactionMapper, times(1)).transactionToResponseAddTransactionDto(transaction);

    }

    @Test
    void shouldFailToAddTransactionWhenTransactionStatusIsNotCompleted() {
        // arrange
        Status status = Status.CANCELED;
        Transaction transaction = new Transaction();
        transaction.setStatus(status);

        RequestAddTransactionDTO request = new RequestAddTransactionDTO();
        request.setStatus(status);

        // when
        when(transactionMapper.requestAddTransactionDtoToTransaction(request)).thenReturn(transaction);

        // assert
        assertThrows(BusinessException.class, () -> transactionsManager.addTransaction(request));

        // verify
        verify(transactionMapper, times(1)).requestAddTransactionDtoToTransaction(request);
        verify(transactionMapper, never()).transactionToResponseAddTransactionDto(any());
        verify(userService, never()).saveUser(any());
        verify(transactionRepository, never()).save(any());


    }

    @Test
    void shouldFailToAddTransactionWhenTransactionCreatedDateIsNotNow() {
        // arrange
        Status status = Status.COMPLETED;
        LocalDate date = LocalDate.now().plusDays(1);
        Transaction transaction = new Transaction();
        transaction.setStatus(status);
        transaction.setCreatedDate(date);

        RequestAddTransactionDTO request = new RequestAddTransactionDTO();
        request.setStatus(status);
        request.setCreatedDate(date);

        // when
        when(transactionMapper.requestAddTransactionDtoToTransaction(request)).thenReturn(transaction);

        // act & assert
        assertThrows(BusinessException.class, () -> transactionsManager.addTransaction(request));

        // verify
        verify(transactionMapper, times(1)).requestAddTransactionDtoToTransaction(request);
        verify(transactionMapper, never()).transactionToResponseAddTransactionDto(any());
        verify(userService, never()).saveUser(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldFailToAddTransactionWhenOfferIsNull() {
        // arrange
        int offerId = 1;
        Boolean offerStatus = false;
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setStatus(offerStatus);

        Status status = Status.COMPLETED;
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction();
        transaction.setOffer(offer);
        transaction.setStatus(status);
        transaction.setCreatedDate(date);

        RequestAddTransactionDTO request = new RequestAddTransactionDTO();
        request.setOfferId(offerId);
        request.setStatus(status);


        // when
        when(transactionMapper.requestAddTransactionDtoToTransaction(request)).thenReturn(transaction);
        when(offerService.getOfferById(offerId)).thenThrow(new BusinessException("Offer can't be null"));

        // act & assert
        assertThrows(BusinessException.class, () -> transactionsManager.addTransaction(request));

        // verify
        verify(transactionMapper, times(1)).requestAddTransactionDtoToTransaction(request);
        verify(transactionMapper, never()).transactionToResponseAddTransactionDto(any());
        verify(userService, never()).saveUser(any());
        verify(offerService, times(1)).getOfferById(offerId);
        verify(transactionRepository, never()).save(any());

    }

    @Test
    void shouldFailToAddTransactionWhenOfferHasNoActiveDiscount() {
        // arrange
        int offerId = 1;
        Boolean offerStatus = true;
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setStatus(offerStatus);

        Status status = Status.COMPLETED;
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction();
        transaction.setOffer(offer);
        transaction.setStatus(status);
        transaction.setCreatedDate(date);

        RequestAddTransactionDTO request = new RequestAddTransactionDTO();
        request.setOfferId(offerId);
        request.setStatus(status);


        // when
        when(transactionMapper.requestAddTransactionDtoToTransaction(request)).thenReturn(transaction);
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(discountService.getOfferDiscountAmount(offer)).thenThrow(new BusinessException(""));

        // act & assert
        assertThrows(BusinessException.class, () -> transactionsManager.addTransaction(request));

        // verify
        verify(transactionMapper, times(1)).requestAddTransactionDtoToTransaction(request);
        verify(transactionMapper, never()).transactionToResponseAddTransactionDto(any());
        verify(userService, never()).saveUser(any());
        verify(offerService, times(1)).getOfferById(offerId);
        verify(transactionRepository, never()).save(any());

    }

    @Test
    void shouldFailToAddTransactionWhenUserIsNull() {
        // arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);


        int offerId = 1;
        Boolean offerStatus = true;
        double offerPrice = 0.0;
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setStatus(offerStatus);
        offer.setPrice(offerPrice);

        Status status = Status.COMPLETED;
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setOffer(offer);
        transaction.setStatus(status);
        transaction.setCreatedDate(date);

        RequestAddTransactionDTO request = new RequestAddTransactionDTO();
        request.setOfferId(offerId);
        request.setUserId(userId);
        request.setStatus(status);



        // when
        when(transactionMapper.requestAddTransactionDtoToTransaction(request)).thenReturn(transaction);
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(discountService.getOfferDiscountAmount(offer)).thenReturn(0.0);
        when(userService.getUserById(userId)).thenThrow(new BusinessException("dasd"));

        // act & assert
        assertThrows(BusinessException.class, () -> transactionsManager.addTransaction(request));

        // verify
        verify(transactionMapper, times(1)).requestAddTransactionDtoToTransaction(request);
        verify(transactionMapper, never()).transactionToResponseAddTransactionDto(any());
        verify(userService, never()).saveUser(any());
        verify(offerService, times(1)).getOfferById(offerId);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldFailToAddTransactionWhenUserHasNotEnoughBalance() {
        // arrange
        int userId = 1;
        double userBalance = 10.0;
        User user = new User();
        user.setId(userId);
        user.setBalance(userBalance);


        int offerId = 1;
        Boolean offerStatus = true;
        double offerPrice = 20.0;
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setStatus(offerStatus);
        offer.setPrice(offerPrice);

        Status status = Status.COMPLETED;
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setOffer(offer);
        transaction.setStatus(status);
        transaction.setCreatedDate(date);

        RequestAddTransactionDTO request = new RequestAddTransactionDTO();
        request.setOfferId(offerId);
        request.setUserId(userId);
        request.setStatus(status);



        // when
        when(transactionMapper.requestAddTransactionDtoToTransaction(request)).thenReturn(transaction);
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        when(discountService.getOfferDiscountAmount(offer)).thenReturn(0.0);
        when(userService.getUserById(userId)).thenReturn(user);

        // act & assert
        assertThrows(BusinessException.class, () -> transactionsManager.addTransaction(request));

        // verify
        verify(transactionMapper, times(1)).requestAddTransactionDtoToTransaction(request);
        verify(transactionMapper, never()).transactionToResponseAddTransactionDto(any());
        verify(userService, never()).saveUser(any());
        verify(offerService, times(1)).getOfferById(offerId);
        verify(transactionRepository, never()).save(any());
    }










}