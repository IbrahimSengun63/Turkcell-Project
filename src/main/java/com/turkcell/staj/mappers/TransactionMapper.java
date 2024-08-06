package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction requestAddTransactionDtoToTransaction(RequestAddTransactionDTO requestAddTransactionDTO);
    ResponseAddTransactionDTO transactionToResponseAddTransactionDto(Transaction transaction);
}
