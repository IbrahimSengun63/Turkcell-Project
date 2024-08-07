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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/v1/add")
    public ResponseEntity<ResponseAddTransactionDTO> addTransaction(@RequestBody @Valid RequestAddTransactionDTO requestAddTransactionDTO) {
        ResponseAddTransactionDTO responseAddTransactionDTO = this.transactionService.addTransaction(requestAddTransactionDTO);
        return ResponseEntity.ok(responseAddTransactionDTO);
    }

    @PostMapping("/v1/update/{id}")
    public ResponseEntity<ResponseUpdateTransactionDTO> updateTransaction(@PathVariable @Valid @Min(value = 1) int id, @RequestBody @Valid RequestUpdateTransactionDTO requestUpdateTransactionDTO) {
        requestUpdateTransactionDTO.setTransactionId(id);
        ResponseUpdateTransactionDTO responseUpdateTransactionDTO = this.transactionService.updateTransaction(requestUpdateTransactionDTO);
        return ResponseEntity.ok(responseUpdateTransactionDTO);
    }

    @GetMapping("/v1/history/{id}")
    public ResponseEntity<GetUserTransactionsWrapper> getHistory(@PathVariable @Valid @Min(value = 1) int id){
        GetUserTransactionsWrapper wrapper = this.transactionService.getHistory(id);
        return ResponseEntity.ok(wrapper);
    }

    @PostMapping("/v1/transaction/{transactionId}/return")
    public ResponseEntity<ResponseReturnTransactionDTO> returnTransaction(
            @PathVariable @Valid @Min(value = 1) int transactionId,
            @RequestParam @Valid @Min(value = 1) int userId){
        ResponseReturnTransactionDTO transactionDTO = this.transactionService.returnTransaction(transactionId, userId);
        return ResponseEntity.ok(transactionDTO);
    }
}
