package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.business.rules.TransactionBusinessRules;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;

import com.turkcell.staj.entities.Transaction;
import com.turkcell.staj.entities.User;
import com.turkcell.staj.mappers.TransactionMapper;
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


    @Override
    public ResponseAddTransactionDTO addTransaction(RequestAddTransactionDTO requestAddTransactionDTO) {
        Transaction transaction = this.transactionMapper.requestAddTransactionDtoToTransaction(requestAddTransactionDTO);
        User user = this.userRepository.findById(transaction.getUser().getId()).orElseThrow();
        TransactionBusinessRules.checkIfUserHasEnoughBalance(user.getBalance(), transaction.getPrice());
        user.setBalance(user.getBalance() - transaction.getPrice());
        this.userRepository.save(user);
        Transaction savedTransaction = this.transactionRepository.save(transaction);
        ResponseAddTransactionDTO responseAddTransactionDTO = this.transactionMapper.transactionToResponseAddTransactionDto(savedTransaction);
        responseAddTransactionDTO.setUserBalanceAfterTransaction(user.getBalance());
        return responseAddTransactionDTO;
    }

}
