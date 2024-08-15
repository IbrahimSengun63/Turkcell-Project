package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.controllers.responseWrappers.GetUserTransactionsWrapper;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.requests.RequestUpdateTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseReturnTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseUpdateTransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CacheConfig(cacheNames = "transactions")
@Tag(name = "Transaction Controller", description = "Manage transactions in the system")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/add")
    @CacheEvict(value = "transaction_list", allEntries = true)
    @Operation(summary = "Add Transaction", description = "Adds a new transaction to the database.")
    public ResponseEntity<ResponseAddTransactionDTO> addTransaction(@RequestBody @Valid RequestAddTransactionDTO requestAddTransactionDTO) {
        ResponseAddTransactionDTO responseAddTransactionDTO = transactionService.addTransaction(requestAddTransactionDTO);
        return ResponseEntity.ok(responseAddTransactionDTO);
    }

    @PutMapping("/update/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = "transaction_list", allEntries = true)
    @Operation(summary = "Update Transaction",
            description = "Updates an existing transaction using the provided transaction ID and update data. " +
                    "This may involve canceling or rejecting completed transactions, " +
                    "which will result in refunding the amount back to the user's balance.")
    public ResponseEntity<ResponseUpdateTransactionDTO> updateTransaction(@PathVariable @Valid @Min(value = 1) int id, @RequestBody @Valid RequestUpdateTransactionDTO requestUpdateTransactionDTO) {
        ResponseUpdateTransactionDTO responseUpdateTransactionDTO = transactionService.updateTransaction(id,requestUpdateTransactionDTO);
        return ResponseEntity.ok(responseUpdateTransactionDTO);
    }

    @GetMapping("/user/{userId}")
    @Cacheable(value = "transaction_list")
    @Operation(summary = "Get User Transaction History", description = "Retrieves the transaction history for a specific user based on the user ID.")
    public ResponseEntity<GetUserTransactionsWrapper> getHistory(@PathVariable @Valid @Min(value = 1) int userId){
        GetUserTransactionsWrapper wrapper = transactionService.getHistory(userId);
        return ResponseEntity.ok(wrapper);
    }

    @PutMapping("/return/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = "transaction_list", allEntries = true)
    @Operation(summary = "Return Transaction",
            description = "Processes the return of a transaction based on the provided transaction ID and user ID. " +
                    "This includes verifying whether the offer was purchased and if it has already been returned. " +
                    "If valid, the amount is credited back to the user's balance.")
    public ResponseEntity<ResponseReturnTransactionDTO> returnTransaction(
            @PathVariable @Valid @Min(value = 1) int id,
            @RequestParam @Valid @Min(value = 1) int userId){
        ResponseReturnTransactionDTO transactionDTO = transactionService.returnTransaction(id, userId);
        return ResponseEntity.ok(transactionDTO);
    }
}
