package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.DiscountService;
import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.business.rules.TransactionBusinessRules;
import com.turkcell.staj.core.enums.Status;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.requests.RequestUpdateTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseUpdateTransactionDTO;
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
import java.util.Optional;

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

    @Test
    void shouldUpdateTransaction() {
        // arrange
        int userId = 1;
        double userBalance = 8.0;
        User user = new User();
        user.setId(userId);
        user.setBalance(userBalance);

        int transactionId = 1;
        Status status = Status.CANCELED;
        double price = 22.0;
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setStatus(status);
        transaction.setPrice(price);
        transaction.setUser(user);

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setId(transactionId);
        updatedTransaction.setUser(user);
        updatedTransaction.setStatus(Status.CANCELED);
        updatedTransaction.setPrice(price);


        RequestUpdateTransactionDTO request = new RequestUpdateTransactionDTO(status);
        request.setStatus(Status.CANCELED);
        ResponseUpdateTransactionDTO response = new ResponseUpdateTransactionDTO();
        response.setTransactionId(transactionId);
        response.setStatus(status);
        response.setUserId(userId);
        response.setPrice(price);

        // when
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        doNothing().when(userService).saveUser(user);
        doNothing().when(transactionMapper).updateTransactionFromRequestUpdateTransactionDTO(request, transaction);
        when(transactionRepository.save(transaction)).thenReturn(updatedTransaction);
        when(transactionMapper.transactionToResponseUpdateTransactionDto(updatedTransaction)).thenReturn(response);

        // act
        ResponseUpdateTransactionDTO result = transactionsManager.updateTransaction(transactionId, request);

        // assert
        assertNotNull(result);
        assertEquals(userBalance, result.getUserBalanceAfterTransaction());

        // verify
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(userService, times(1)).saveUser(user);
        verify(transactionMapper, times(1)).updateTransactionFromRequestUpdateTransactionDTO(request, transaction);
        verify(transactionRepository, times(1)).save(transaction);
        verify(transactionMapper, times(1)).transactionToResponseUpdateTransactionDto(updatedTransaction);

    }

    @Test
    void shouldUpdateTransactionAndUpdateBalanceIfTransactionStatusChangedFromCompleted() {
        // arrange
        int userId = 1;
        double userBalance = 8.0;
        User user = new User();
        user.setId(userId);
        user.setBalance(userBalance);

        int transactionId = 1;
        Status status = Status.COMPLETED;
        double price = 22.0;
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setStatus(status);
        transaction.setPrice(price);
        transaction.setUser(user);

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setId(transactionId);
        updatedTransaction.setUser(user);
        updatedTransaction.setStatus(Status.CANCELED);
        updatedTransaction.setPrice(price);


        RequestUpdateTransactionDTO request = new RequestUpdateTransactionDTO(status);
        request.setStatus(Status.CANCELED);
        ResponseUpdateTransactionDTO response = new ResponseUpdateTransactionDTO();
        response.setTransactionId(transactionId);
        response.setStatus(status);
        response.setUserId(userId);
        response.setPrice(price);

        // when
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        doNothing().when(userService).saveUser(user);
        doNothing().when(transactionMapper).updateTransactionFromRequestUpdateTransactionDTO(request, transaction);
        when(transactionRepository.save(transaction)).thenReturn(updatedTransaction);
        when(transactionMapper.transactionToResponseUpdateTransactionDto(updatedTransaction)).thenReturn(response);

        // act
        ResponseUpdateTransactionDTO result = transactionsManager.updateTransaction(transactionId, request);

        // assert
        assertNotNull(result);
        assertEquals(userBalance + price, result.getUserBalanceAfterTransaction());

        // verify
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(userService, times(1)).saveUser(user);
        verify(transactionMapper, times(1)).updateTransactionFromRequestUpdateTransactionDTO(request, transaction);
        verify(transactionRepository, times(1)).save(transaction);
        verify(transactionMapper, times(1)).transactionToResponseUpdateTransactionDto(updatedTransaction);

    }

    @Test
    void shouldFailToUpdateTransactionWhenStatusIsCompleted() {
        // arrange
        int transactionId = 1;

        RequestUpdateTransactionDTO request = new RequestUpdateTransactionDTO(Status.COMPLETED);

        // act & assert
        assertThrows(BusinessException.class, () -> transactionsManager.updateTransaction(transactionId, request));

        verify(transactionRepository, never()).findById(transactionId);
    }

    @Test
    void shouldFailToUpdateTransactionWhenTransactionIsNull() {
        // arrange
        int transactionId = 1;

        RequestUpdateTransactionDTO request = new RequestUpdateTransactionDTO(Status.CANCELED);

        // act & assert
        assertThrows(BusinessException.class, () -> transactionsManager.updateTransaction(transactionId, request));

        //verify
        verify(transactionRepository,times(1)).findById(transactionId);
        verify(userService,never()).saveUser(any());
    }


}