package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.controllers.responseWrappers.GetUserTransactionsWrapper;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.requests.RequestUpdateTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseReturnTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseUpdateTransactionDTO;
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
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/add")
    @CacheEvict(value = "transaction_list", allEntries = true)
    public ResponseEntity<ResponseAddTransactionDTO> addTransaction(@RequestBody @Valid RequestAddTransactionDTO requestAddTransactionDTO) {
        ResponseAddTransactionDTO responseAddTransactionDTO = transactionService.addTransaction(requestAddTransactionDTO);
        return ResponseEntity.ok(responseAddTransactionDTO);
    }

    @PutMapping("/update/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = "transaction_list", allEntries = true)
    public ResponseEntity<ResponseUpdateTransactionDTO> updateTransaction(@PathVariable @Valid @Min(value = 1) int id, @RequestBody @Valid RequestUpdateTransactionDTO requestUpdateTransactionDTO) {
        ResponseUpdateTransactionDTO responseUpdateTransactionDTO = transactionService.updateTransaction(id,requestUpdateTransactionDTO);
        return ResponseEntity.ok(responseUpdateTransactionDTO);
    }

    @GetMapping("/user/{userId}")
    @Cacheable(value = "transaction_list")
    public ResponseEntity<GetUserTransactionsWrapper> getHistory(@PathVariable @Valid @Min(value = 1) int userId){
        GetUserTransactionsWrapper wrapper = transactionService.getHistory(userId);
        return ResponseEntity.ok(wrapper);
    }

    @PutMapping("/return/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = "transaction_list", allEntries = true)
    public ResponseEntity<ResponseReturnTransactionDTO> returnTransaction(
            @PathVariable @Valid @Min(value = 1) int id,
            @RequestParam @Valid @Min(value = 1) int userId){
        ResponseReturnTransactionDTO transactionDTO = transactionService.returnTransaction(id, userId);
        return ResponseEntity.ok(transactionDTO);
    }
}
