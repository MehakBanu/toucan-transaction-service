package com.example.transactionstarter.controller;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.UpdateStatusRequest;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // A. Create transaction
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        Transaction transaction =
                transactionService.createTransaction(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }

    // B. Get transaction
    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(
            @PathVariable @NotBlank String transactionId) {

        return ResponseEntity.ok(
                transactionService.getTransaction(transactionId)
        );
    }

    // C. Update transaction status
    @PatchMapping("/transactions/{transactionId}/status")
    public ResponseEntity<Transaction> updateTransactionStatus(
            @PathVariable String transactionId,
            @Valid @RequestBody UpdateStatusRequest request) {

        Transaction transaction =
                transactionService.updateTransactionStatus(
                        transactionId,
                        request.getTransactionStatus()
                );

        return ResponseEntity.ok(transaction);
    }

    // D. Get customer transactions
    @GetMapping("/customers/{customerId}/transactions")
    public ResponseEntity<List<Transaction>> getCustomerTransactions(
            @PathVariable @NotBlank String customerId) {

        return ResponseEntity.ok(
                transactionService.getCustomerTransactions(customerId)
        );
    }
}