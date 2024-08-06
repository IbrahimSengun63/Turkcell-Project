package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;

import com.turkcell.staj.entities.Transaction;
import com.turkcell.staj.mappers.TransactionMapper;
import com.turkcell.staj.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionManager implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public ResponseAddTransactionDTO addTransaction(RequestAddTransactionDTO requestAddTransactionDTO) {
        Transaction transaction = this.transactionMapper.requestAddTransactionDtoToTransaction(requestAddTransactionDTO);
        Transaction savedTransaction = this.transactionRepository.save(transaction);
        return this.transactionMapper.transactionToResponseAddTransactionDto(savedTransaction);
    }
}
