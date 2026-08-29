package com.example.transactionstarter.transaction.repository;

import com.example.transactionstarter.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByCustomerId(String customerId);
}