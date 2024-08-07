package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.business.rules.TransactionBusinessRules;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.requests.RequestUpdateTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;

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

}
