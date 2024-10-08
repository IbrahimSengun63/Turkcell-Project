package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.DiscountService;
import com.turkcell.staj.business.abstracts.OfferService;
import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.business.rules.TransactionBusinessRules;
import com.turkcell.staj.controllers.responseWrappers.GetUserTransactionsWrapper;
import com.turkcell.staj.core.enums.Status;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.requests.RequestUpdateTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseGetAllUserTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseReturnTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseUpdateTransactionDTO;
import com.turkcell.staj.entities.Offer;
import com.turkcell.staj.entities.Transaction;
import com.turkcell.staj.entities.User;
import com.turkcell.staj.mappers.TransactionMapper;
import com.turkcell.staj.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionsManager implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserService userService;
    private final OfferService offerService;
    private final DiscountService discountService;

    @Override
    public ResponseAddTransactionDTO addTransaction(RequestAddTransactionDTO requestAddTransactionDTO) {
        Transaction transaction = transactionMapper.requestAddTransactionDtoToTransaction(requestAddTransactionDTO);
        // Checks
        TransactionBusinessRules.checkIfStatusCompleted(transaction.getStatus());
        Offer offer = offerService.getOfferById(transaction.getOffer().getId());
        TransactionBusinessRules.checkIfOfferIsPurchasable(offer.getStatus());
        // Calculate transaction amount
        double discountAmount = discountService.getOfferDiscountAmount(offer);
        transaction.setPrice(offer.getPrice() - discountAmount);
        // Check if user has enough balance
        User user = userService.getUserById(transaction.getUser().getId());
        TransactionBusinessRules.checkIfUserHasEnoughBalance(user.getBalance(), transaction.getPrice());
        // Update user balance
        user.setBalance(user.getBalance() - transaction.getPrice());
        userService.saveUser(user);
        // Save transaction record
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction with ID {} has been successfully saved to the database.", savedTransaction.getId());
        ResponseAddTransactionDTO responseAddTransactionDTO = transactionMapper.transactionToResponseAddTransactionDto(savedTransaction);
        // Set fields not included in the entity
        responseAddTransactionDTO.setUserBalanceAfterTransaction(user.getBalance());
        responseAddTransactionDTO.setOfferPrice(offer.getPrice());
        return responseAddTransactionDTO;
    }

    @Override
    public ResponseUpdateTransactionDTO updateTransaction(int id, RequestUpdateTransactionDTO requestUpdateTransactionDTO) {
        // check status
        TransactionBusinessRules.checkIfStatusCanceledOrRejected(requestUpdateTransactionDTO.getStatus());
        // get old transaction
        Transaction transaction = getTransactionById(id);
        // update user balance only if status is completed
        transaction.getUser().setBalance(TransactionBusinessRules.updateBalanceIfTransactionStatusChangedFromCompleted(
                transaction.getStatus(),
                transaction.getUser().getBalance(),
                transaction.getPrice()));
        // save updated user
        userService.saveUser(transaction.getUser());
        // convert transaction from request
        transactionMapper.updateTransactionFromRequestUpdateTransactionDTO(requestUpdateTransactionDTO, transaction);
        // update transaction
        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction with ID {} has been successfully updated and saved to the database.", updatedTransaction.getId());
        ResponseUpdateTransactionDTO responseUpdateTransactionDTO = transactionMapper.transactionToResponseUpdateTransactionDto(updatedTransaction);
        responseUpdateTransactionDTO.setUserBalanceAfterTransaction(updatedTransaction.getUser().getBalance());
        return responseUpdateTransactionDTO;
    }

    @Override
    public GetUserTransactionsWrapper getHistory(int userId) {
        // check user
        userService.getUserById(userId);
        // get all transaction belong to user and status is completed
        log.info("Retrieved transactions with userID {} from the database.", userId);
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        // calculate total purchase history
        double total = TransactionBusinessRules.calculateUserTotalPurchase(transactions);
        List<ResponseGetAllUserTransactionDTO> response = transactionMapper.transactionListToResponseDtoList(transactions);
        GetUserTransactionsWrapper wrapper = new GetUserTransactionsWrapper();
        wrapper.setUserTransactions(response);
        wrapper.setTotalPurchaseHistory(total);
        return wrapper;
    }

    @Override
    public ResponseReturnTransactionDTO returnTransaction(int transactionId, int userId) {
        // Get transaction
        Transaction transaction = getTransactionById(transactionId);
        // Checks
        TransactionBusinessRules.checkIfTransactionBelongsToUser(transaction.getUser().getId(), userId);
        TransactionBusinessRules.checkIfReturnStatusCompleted(transaction.getStatus());
        // Get user
        User user = userService.getUserById(userId);
        // Calculate balance after return
        double resultBalance = TransactionBusinessRules.updateBalanceIfTransactionStatusChangedFromCompleted(
                transaction.getStatus(),
                user.getBalance(),
                transaction.getPrice()
        );
        // Save new balance to db
        user.setBalance(resultBalance);
        userService.saveUser(user);
        // Mark transaction as returned
        transaction.setStatus(Status.RETURNED);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction with ID {} has been successfully updated and saved to the database.", updatedTransaction.getId());
        ResponseReturnTransactionDTO responseReturnTransactionDTO = transactionMapper.transactionToResponseReturnTransactionDto(updatedTransaction);
        responseReturnTransactionDTO.setUserBalanceAfterTransaction(resultBalance);
        return responseReturnTransactionDTO;
    }

    @Override
    public boolean checkIfUserPurchasedOffer(int userId, int offerId) {
        return transactionRepository.existsByUserIdAndOfferId(userId, offerId);
    }

    @Override
    public Transaction getTransactionById(int id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Transaction with ID {} successfully retrieved from the database.", id);
                    return new BusinessException("Transaction can't be null");
                });
        log.info("Transaction with ID {} successfully retrieved from the database.", id);
        return transaction;
    }

}
