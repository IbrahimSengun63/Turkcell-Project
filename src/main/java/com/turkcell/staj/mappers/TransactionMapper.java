package com.turkcell.staj.mappers;

import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.requests.RequestUpdateTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseGetAllUserTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseReturnTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseUpdateTransactionDTO;
import com.turkcell.staj.entities.Transaction;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "offerId", target = "offer.id")
    Transaction requestAddTransactionDtoToTransaction(RequestAddTransactionDTO requestAddTransactionDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "transaction.id", target = "transactionId")
    ResponseAddTransactionDTO transactionToResponseAddTransactionDto(Transaction transaction);
    
    void updateTransactionFromRequestUpdateTransactionDTO(RequestUpdateTransactionDTO requestUpdateTransactionDTO, @MappingTarget Transaction transaction);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "transaction.id", target = "transactionId")
    ResponseUpdateTransactionDTO transactionToResponseUpdateTransactionDto(Transaction transaction);


    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "offer.id", target = "offerId")
    ResponseGetAllUserTransactionDTO transactionToGetAllUserTransactionDto(Transaction transaction);

    List<ResponseGetAllUserTransactionDTO> transactionListToResponseDtoList(List<Transaction> transactionList);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "id", target = "transactionId")
    ResponseReturnTransactionDTO transactionToResponseReturnTransactionDto(Transaction transaction);


}


