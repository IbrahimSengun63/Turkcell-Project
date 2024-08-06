package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.requests.RequestUpdateTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseUpdateTransactionDTO;
import com.turkcell.staj.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "offerId", target = "offer.id")
    Transaction requestAddTransactionDtoToTransaction(RequestAddTransactionDTO requestAddTransactionDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "transaction.id", target = "transactionId")
    ResponseAddTransactionDTO transactionToResponseAddTransactionDto(Transaction transaction);


    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "offerId", target = "offer.id")
    @Mapping(source = "transactionId", target = "id")
    Transaction requestUpdateTransactionDtoToTransaction(RequestUpdateTransactionDTO requestUpdateTransactionDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "transaction.id", target = "transactionId")
    ResponseUpdateTransactionDTO transactionToResponseUpdateTransactionDto(Transaction transaction);
}
