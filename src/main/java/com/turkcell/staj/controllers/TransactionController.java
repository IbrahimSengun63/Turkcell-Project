package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/v1/transaction/add")
    public ResponseEntity<ResponseAddTransactionDTO> addTransaction(@RequestBody @Valid RequestAddTransactionDTO requestAddTransactionDTO) {
        ResponseAddTransactionDTO responseAddTransactionDTO = this.transactionService.addTransaction(requestAddTransactionDTO);
        return ResponseEntity.ok(responseAddTransactionDTO);
    }
}
