package com.turkcell.staj.business.concretes;

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
import com.turkcell.staj.repositories.OfferRepository;
import com.turkcell.staj.repositories.TransactionRepository;
import com.turkcell.staj.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionsManager implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserService userService;
    private final OfferService offerService;

    @Override
    public ResponseAddTransactionDTO addTransaction(RequestAddTransactionDTO requestAddTransactionDTO) {
        // req to transaction mapping
        Transaction transaction = transactionMapper.requestAddTransactionDtoToTransaction(requestAddTransactionDTO);
        // checks
        TransactionBusinessRules.checkIfStatusCompleted(transaction.getStatus());
        TransactionBusinessRules.checkIfDateIsCorrect(transaction.getCreatedDate());
        // get offer from db
        Offer offer = offerService.getOfferById(transaction.getOffer().getId());
        // checks offer status
        TransactionBusinessRules.checkIfOfferIsActive(offer.isStatus());
        // set transaction price with offer price
        transaction.setPrice(offer.getPrice());
        // get user from db
        User user = userService.getUserById(transaction.getUser().getId());
        // check user balance
        TransactionBusinessRules.checkIfUserHasEnoughBalance(user.getBalance(), transaction.getPrice());
        // set user balance
        user.setBalance(user.getBalance() - transaction.getPrice());
        // update user
        userService.saveUser(user);
        // add transaction
        Transaction savedTransaction = transactionRepository.save(transaction);
        ResponseAddTransactionDTO responseAddTransactionDTO = transactionMapper.transactionToResponseAddTransactionDto(savedTransaction);
        responseAddTransactionDTO.setUserBalanceAfterTransaction(user.getBalance());
        return responseAddTransactionDTO;
    }

    @Override
    public ResponseUpdateTransactionDTO updateTransaction(int id, RequestUpdateTransactionDTO requestUpdateTransactionDTO) {
        // check status
        TransactionBusinessRules.checkIfStatusCanceledOrRejected(requestUpdateTransactionDTO.getStatus());
        // get user and check if updatable user exists
        User user = userService.getUserById(requestUpdateTransactionDTO.getUserId());
        // check if updatable offer exists
        offerService.getOfferById(requestUpdateTransactionDTO.getOfferId());
        // get old transaction
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new BusinessException("Transaction can't be null"));
        // update user balance only if status is completed
        user.setBalance(TransactionBusinessRules.updateBalanceIfTransactionStatusChangedFromCompleted(transaction.getStatus(), user.getBalance(), transaction.getPrice()));
        // save updated user
        userService.saveUser(user);
        // convert transaction from request
        transactionMapper.updateTransactionFromRequestUpdateTransactionDTO(requestUpdateTransactionDTO, transaction);
        // update transaction
        Transaction updatedTransaction = transactionRepository.save(transaction);
        ResponseUpdateTransactionDTO responseUpdateTransactionDTO = transactionMapper.transactionToResponseUpdateTransactionDto(updatedTransaction);
        responseUpdateTransactionDTO.setUserBalanceAfterTransaction(user.getBalance());
        return responseUpdateTransactionDTO;
    }

    @Override
    public GetUserTransactionsWrapper getHistory(int userId) {
        // check user
        userService.getUserById(userId);
        // get all transaction belong to user and status is completed
        List<Transaction> transactions = transactionRepository.findByUserIdAndStatus(userId, Status.COMPLETED);
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
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new BusinessException("Transaction can't be null"));
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
        ResponseReturnTransactionDTO responseReturnTransactionDTO = transactionMapper.transactionToResponseReturnTransactionDto(updatedTransaction);
        responseReturnTransactionDTO.setUserBalanceAfterTransaction(resultBalance);
        return responseReturnTransactionDTO;
    }

    @Override
    public boolean checkIfUserPurchasedOffer(int userId, int offerId) {
        return transactionRepository.existsByUserIdAndOfferId(userId, offerId);
    }

}
