package com.example.transactionstarter.transaction;

import com.example.transactionstarter.transaction.entity.Transaction;
import com.example.transactionstarter.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void cleanDatabase() {
        transactionRepository.deleteAll();
    }

    // 1. Valid transaction should be created successfully
    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {

        String request = """
                {
                    "transactionId": "TXN001",
                    "customerId": "CUST001",
                    "amount": 5000,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId", is("TXN001")))
                .andExpect(jsonPath("$.customerId", is("CUST001")))
                .andExpect(jsonPath("$.currency", is("INR")))
                .andExpect(jsonPath("$.transactionType", is("PAYMENT")))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    // 2. Invalid transaction should be rejected
    @Test
    void shouldRejectInvalidTransaction() throws Exception {

        String request = """
                {
                    "transactionId": "TXN002",
                    "customerId": "CUST001",
                    "amount": -100,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Amount must be greater than 0")));
    }

    // 3. Duplicate transaction ID should be rejected
    @Test
    void shouldRejectDuplicateTransactionId() throws Exception {

        Transaction transaction = new Transaction(
                "TXN003",
                "CUST001",
                new BigDecimal("1000"),
                "INR",
                "PAYMENT",
                "PENDING"
        );

        transactionRepository.save(transaction);

        String request = """
                {
                    "transactionId": "TXN003",
                    "customerId": "CUST002",
                    "amount": 2000,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Transaction ID already exists")));
    }

    // 4. Request for a transaction that does not exist
    @Test
    void shouldReturnNotFoundForMissingTransaction() throws Exception {

        mockMvc.perform(get("/transactions/TXN999"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.error", is("Transaction not found: TXN999"))
                );
    }

    // 5. Blank transaction ID should be rejected
    @Test
    void shouldRejectBlankTransactionId() throws Exception {

        String request = """
                {
                    "transactionId": "",
                    "customerId": "CUST001",
                    "amount": 1000,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.error", is("Transaction ID is required"))
                );
    }

    // 6. Blank customer ID should be rejected
    @Test
    void shouldRejectBlankCustomerId() throws Exception {

        String request = """
                {
                    "transactionId": "TXN004",
                    "customerId": "",
                    "amount": 1000,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.error", is("Customer ID is required"))
                );
    }

    // 7. Invalid status should be rejected
    @Test
    void shouldRejectInvalidStatus() throws Exception {

        Transaction transaction = new Transaction(
                "TXN005",
                "CUST001",
                new BigDecimal("1000"),
                "INR",
                "PAYMENT",
                "PENDING"
        );

        transactionRepository.save(transaction);

        mockMvc.perform(
                put("/transactions/TXN005/status")
                        .param("status", "INVALID")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
                jsonPath(
                        "$.error",
                        is("New status must be COMPLETED or FAILED")
                )
        );
    }
}