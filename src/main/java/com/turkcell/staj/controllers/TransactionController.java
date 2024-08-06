package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.TransactionService;
import com.turkcell.staj.dtos.transaction.requests.RequestAddTransactionDTO;
import com.turkcell.staj.dtos.transaction.responses.ResponseAddTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<ResponseAddTransactionDTO> addTransaction(@RequestBody RequestAddTransactionDTO requestAddTransactionDTO) {
        ResponseAddTransactionDTO responseAddTransactionDTO = this.transactionService.addTransaction(requestAddTransactionDTO);
        return ResponseEntity.ok(responseAddTransactionDTO);
    }
}
