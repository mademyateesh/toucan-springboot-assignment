# Customer Transaction Service

## Problem Understanding

This project implements a REST-based transaction-processing service for managing customer transactions.

Each transaction contains:

- Transaction ID
- Customer ID
- Amount
- Currency
- Transaction Type
- Transaction Status

The application supports four operations:

1. Create a transaction
2. Get a transaction by Transaction ID
3. Update the status of a transaction
4. Get all transactions for a Customer ID

## Technology Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- JUnit / Spring Boot Test

## Project Structure

The application is organized into the following layers:

- `controller` - handles REST API requests and responses
- `service` - contains business logic and validation
- `repository` - handles database operations
- `entity` - represents transaction data
- `exception` - handles application exceptions and error responses

## Validation Rules

The following validation rules are applied when creating a transaction:

- Transaction ID is required and must not be blank.
- Customer ID is required and must not be blank.
- Amount is required and must be greater than 0.
- Currency is required and must not be blank.
- Transaction Type is required and must not be blank.
- Transaction Status is required and must not be blank.
- Transaction ID must be unique.

For status updates:

- Transaction ID is required.
- New status is required.
- The new status must be `COMPLETED` or `FAILED`.
- A transaction that is already `COMPLETED` or `FAILED` cannot be changed.

## Status Transition Rules

The transaction lifecycle is:

```text
PENDING -> COMPLETED    Allowed
PENDING -> FAILED       Allowed

COMPLETED -> any status  Not allowed
FAILED -> any status     Not allowed
```

## API Endpoints

### 1. Create Transaction

```http
POST /transactions
```

Example request:

```json
{
  "transactionId": "TXN001",
  "customerId": "CUST001",
  "amount": 5000,
  "currency": "INR",
  "transactionType": "PAYMENT",
  "status": "PENDING"
}
```

Successful response:

```text
201 Created
```

### 2. Get Transaction

```http
GET /transactions/{transactionId}
```

Example:

```http
GET /transactions/TXN001
```

If the transaction does not exist:

```text
404 Not Found
```

### 3. Update Transaction Status

```http
PUT /transactions/{transactionId}/status?status=COMPLETED
```

Example:

```http
PUT /transactions/TXN001/status?status=COMPLETED
```

### 4. Get Customer Transactions

```http
GET /transactions/customer/{customerId}
```

Example:

```http
GET /transactions/customer/CUST001
```

If the customer has no transactions, an empty list is returned.

## Error Handling

The application uses centralized exception handling.

| Situation | HTTP Status |
|---|---|
| Invalid input | 400 Bad Request |
| Transaction not found | 404 Not Found |
| Duplicate Transaction ID | 409 Conflict |

Error responses contain an `error` field describing the problem.

## Testing

The project contains automated tests covering:

1. Successful transaction creation
2. Rejection of an invalid transaction amount
3. Rejection of a duplicate Transaction ID
4. Rejection of a non-existent transaction lookup
5. Rejection of a blank Transaction ID
6. Rejection of a blank Customer ID
7. Rejection of an invalid status update

The original Spring Boot context test is also retained.

Run the complete test suite on Windows using:

```bat
mvnw.cmd clean test
```

The latest verified result is:

```text
Tests run: 8
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## Known Limitations

- H2 is an in-memory database, so data is lost when the application stops.
- Currency and transaction type are currently validated for presence, not against a fixed reference list.
- The application does not implement authentication or authorization.
- Customer transaction lookup returns an empty list when no transactions are found.

## Improvements With More Time

With more time, I would consider:

- Using stronger domain types or enums for status and transaction type.
- Adding more detailed validation.
- Standardizing the error-response structure.
- Adding pagination for customer transaction lookup.
- Using a persistent database for production environments.
- Adding more tests for all status transitions.

## How to Run

On Windows, run:

```bat
mvnw.cmd clean test
```

To start the application:

```bat
mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```