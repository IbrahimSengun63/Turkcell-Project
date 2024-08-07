package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.TransactionService;
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
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;

    @Override
    public ResponseAddTransactionDTO addTransaction(RequestAddTransactionDTO requestAddTransactionDTO) {
        Transaction transaction = this.transactionMapper.requestAddTransactionDtoToTransaction(requestAddTransactionDTO);
        TransactionBusinessRules.checkIfStatusCompleted(transaction.getStatus());
        TransactionBusinessRules.checkIfDateIsCorrect(transaction.getCreatedDate());
        Offer offer = this.offerRepository.findById(transaction.getOffer().getId()).orElseThrow(() -> new BusinessException("Offer can't be null"));
        TransactionBusinessRules.checkIfOfferIsActive(offer.isStatus());
        transaction.setPrice(offer.getPrice());
        User user = this.userRepository.findById(transaction.getUser().getId()).orElseThrow(() -> new BusinessException("User can't be null"));
        TransactionBusinessRules.checkIfUserHasEnoughBalance(user.getBalance(), transaction.getPrice());
        user.setBalance(user.getBalance() - transaction.getPrice());
        this.userRepository.save(user);
        Transaction savedTransaction = this.transactionRepository.save(transaction);
        ResponseAddTransactionDTO responseAddTransactionDTO = this.transactionMapper.transactionToResponseAddTransactionDto(savedTransaction);
        responseAddTransactionDTO.setUserBalanceAfterTransaction(user.getBalance());
        return responseAddTransactionDTO;
    }

    @Override
    public ResponseUpdateTransactionDTO updateTransaction(RequestUpdateTransactionDTO requestUpdateTransactionDTO) {
        TransactionBusinessRules.checkIfStatusCanceledOrRejected(requestUpdateTransactionDTO.getStatus());
        Transaction getTransaction = this.transactionRepository.findById(requestUpdateTransactionDTO.getTransactionId()).orElseThrow(() -> new BusinessException("Transaction can't be null"));
        User user = this.userRepository.findById(getTransaction.getUser().getId()).orElseThrow(() -> new BusinessException("User can't be null"));
        user.setBalance(TransactionBusinessRules.updateBalanceIfTransactionStatusChangedFromCompleted(getTransaction.getStatus(),user.getBalance(),getTransaction.getPrice()));
        this.userRepository.save(user);
        this.transactionMapper.updateTransactionFromRequestUpdateTransactionDTO(requestUpdateTransactionDTO,getTransaction);
        Transaction updatedTransaction = this.transactionRepository.save(getTransaction);
        ResponseUpdateTransactionDTO responseUpdateTransactionDTO = this.transactionMapper.transactionToResponseUpdateTransactionDto(updatedTransaction);
        responseUpdateTransactionDTO.setUserBalanceAfterTransaction(user.getBalance());
        return responseUpdateTransactionDTO;
    }

    @Override
    public GetUserTransactionsWrapper getHistory(int userId) {
        this.userRepository.findById(userId).orElseThrow(() -> new BusinessException("User can't be null"));
        List<Transaction> transactions = this.transactionRepository.findByUserIdAndStatus(userId, Status.COMPLETED);
        double total = TransactionBusinessRules.calculateUserTotalPurchase(transactions);
        List<ResponseGetAllUserTransactionDTO> response = this.transactionMapper.transactionListToResponseDtoList(transactions);
        GetUserTransactionsWrapper wrapper = new GetUserTransactionsWrapper();
        wrapper.setUserTransactions(response);
        wrapper.setTotalPurchaseHistory(total);
        return wrapper;
    }

    @Override
    public ResponseReturnTransactionDTO returnTransaction(int transactionId, int userId) {
        // Get transaction
        Transaction transaction = this.transactionRepository.findById(transactionId).orElseThrow(() -> new BusinessException("Transaction can't be null"));
        // Checks
        TransactionBusinessRules.checkIfTransactionBelongsToUser(transaction.getUser().getId(),userId);
        TransactionBusinessRules.checkIfReturnStatusCompleted(transaction.getStatus());
        // Get user
        User user = this.userRepository.findById(userId).orElseThrow(() -> new BusinessException("User can't be null"));
        // Calculate balance after return
        double resultBalance = TransactionBusinessRules.updateBalanceIfTransactionStatusChangedFromCompleted(
                transaction.getStatus(),
                user.getBalance(),
                transaction.getPrice()
                );
        // Save new balance to db
        user.setBalance(resultBalance);
        this.userRepository.save(user);
        // Mark transaction as returned
        transaction.setStatus(Status.RETURNED);
        Transaction updatedTransaction = this.transactionRepository.save(transaction);
        ResponseReturnTransactionDTO responseReturnTransactionDTO = this.transactionMapper.transactionToResponseReturnTransactionDto(updatedTransaction);
        responseReturnTransactionDTO.setUserBalanceAfterTransaction(resultBalance);
        return responseReturnTransactionDTO;
    }

}
