package com.example.transactionstarter.transaction.service;

import com.example.transactionstarter.transaction.entity.Transaction;
import com.example.transactionstarter.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import com.example.transactionstarter.transaction.exception.DuplicateTransactionException;
import com.example.transactionstarter.transaction.exception.TransactionNotFoundException;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction) {

        if (transaction == null) {
            throw new IllegalArgumentException("Transaction must not be null");
        }

        if (transaction.getTransactionId() == null ||
                transaction.getTransactionId().isBlank()) {
            throw new IllegalArgumentException("Transaction ID is required");
        }

        if (transaction.getCustomerId() == null ||
                transaction.getCustomerId().isBlank()) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        if (transaction.getAmount() == null ||
                transaction.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (transaction.getCurrency() == null ||
                transaction.getCurrency().isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }

        if (transaction.getTransactionType() == null ||
                transaction.getTransactionType().isBlank()) {
            throw new IllegalArgumentException("Transaction type is required");
        }

        if (transaction.getStatus() == null ||
                transaction.getStatus().isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }

        if (transactionRepository.existsById(transaction.getTransactionId())) {
            throw new DuplicateTransactionException("Transaction ID already exists");
        }

        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(String transactionId) {

    	return transactionRepository.findById(transactionId)
            	.orElseThrow(() ->
                   	 new TransactionNotFoundException(
                            	"Transaction not found: " + transactionId));
   }

    public Transaction updateStatus(String transactionId, String newStatus) {

    if (transactionId == null || transactionId.isBlank()) {
        throw new IllegalArgumentException("Transaction ID is required");
    }

    if (newStatus == null || newStatus.isBlank()) {
        throw new IllegalArgumentException("New status is required");
    }

    Transaction transaction = getTransaction(transactionId);

    if ("COMPLETED".equals(transaction.getStatus()) ||
            "FAILED".equals(transaction.getStatus())) {
        throw new IllegalArgumentException(
                "Cannot update status of a completed or failed transaction");
    }

    if (!"COMPLETED".equals(newStatus) &&
            !"FAILED".equals(newStatus)) {
        throw new IllegalArgumentException(
                "New status must be COMPLETED or FAILED");
    }

    transaction.setStatus(newStatus);

    return transactionRepository.save(transaction);
    }

    public List<Transaction> getCustomerTransactions(String customerId) {

    if (customerId == null || customerId.isBlank()) {
        throw new IllegalArgumentException("Customer ID is required");
    }

    return transactionRepository.findByCustomerId(customerId);
}
}