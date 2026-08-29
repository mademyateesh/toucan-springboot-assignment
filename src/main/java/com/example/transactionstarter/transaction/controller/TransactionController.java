package com.example.transactionstarter.transaction.controller;

import com.example.transactionstarter.transaction.entity.Transaction;
import com.example.transactionstarter.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // 1. Create transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody Transaction transaction) {

        Transaction created = transactionService.createTransaction(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 2. Get transaction by ID
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(
            @PathVariable String transactionId) {

        return ResponseEntity.ok(
                transactionService.getTransaction(transactionId)
        );
    }

    // 3. Update transaction status
    @PutMapping("/{transactionId}/status")
    public ResponseEntity<Transaction> updateStatus(
            @PathVariable String transactionId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                transactionService.updateStatus(transactionId, status)
        );
    }

    // 4. Get all transactions for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getCustomerTransactions(
            @PathVariable String customerId) {

        return ResponseEntity.ok(
                transactionService.getCustomerTransactions(customerId)
        );
    }
}