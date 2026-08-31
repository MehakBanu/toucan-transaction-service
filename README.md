# Customer Transaction Processing Service

A Java Spring Boot REST API for managing customer transactions.

## 1. Overview

This project implements the four operations required in the engineering
challenge:

1. Create a transaction
2. Get a transaction by Transaction ID
3. Update the status of a transaction
4. Get all transactions for a Customer ID

The application uses:

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 embedded database
- JUnit / Spring Boot Test

The application follows a simple Controller-Service-Repository structure.

---

## 2. Transaction Fields

Each transaction contains:

| Field | Description |
|---|---|
| Transaction ID | Unique identifier for the transaction |
| Customer ID | Identifier of the customer |
| Amount | Transaction amount |
| Currency | Currency of the transaction |
| Transaction Type | Type of transaction |
| Transaction Status | Current status of the transaction |

---

## 3. Validation Rules

The following validation rules were defined for this implementation.

### Transaction ID
- Required.
- Must not be blank.
- Must be unique.

### Customer ID
- Required.
- Must not be blank.

### Amount
- Required.
- Must be greater than `0`.

### Currency
- Required.
- Must not be blank.
- Stored in uppercase.

No specific permitted currency list was provided in the assignment materials
received, so no fixed currency list is enforced.

### Transaction Type
- Required.
- Must not be blank.
- Stored in uppercase.

No specific permitted transaction type list was provided in the assignment
materials received, so no fixed transaction type list is enforced.

### Transaction Status

The supported statuses are:

- `PENDING`
- `COMPLETED`
- `FAILED`
- `CANCELLED`

The status is stored in uppercase.

### Status Transition Rules

A transaction in `PENDING` status can move to:

```text
PENDING
   ├──> COMPLETED
   ├──> FAILED
   └──> CANCELLED
```

`COMPLETED`, `FAILED` and `CANCELLED` are treated as final states and cannot
be changed again.

This prevents a transaction that has reached a final state from being
modified later.

---

## 4. API Endpoints

### Create Transaction

**POST**

```text
/api/transactions
```

Example request:

```json
{
  "transactionId": "TXN001",
  "customerId": "CUS001",
  "amount": 1000.00,
  "currency": "INR",
  "transactionType": "PAYMENT",
  "transactionStatus": "PENDING"
}
```

Response:

```text
201 CREATED
```

Duplicate Transaction ID:

```text
409 CONFLICT
```

Invalid request:

```text
400 BAD REQUEST
```

---

### Get Transaction

**GET**

```text
/api/transactions/{transactionId}
```

Example:

```text
/api/transactions/TXN001
```

If the transaction does not exist:

```text
404 NOT FOUND
```

---

### Update Transaction Status

**PATCH**

```text
/api/transactions/{transactionId}/status
```

Example:

```text
/api/transactions/TXN001/status
```

Request:

```json
{
  "transactionStatus": "COMPLETED"
}
```

Response:

```text
200 OK
```

Invalid status or status transition:

```text
400 BAD REQUEST
```

---

### Get Customer Transactions

**GET**

```text
/api/customers/{customerId}/transactions
```

Example:

```text
/api/customers/CUS001/transactions
```

Returns all transactions associated with the specified Customer ID.

---

## 5. Error Handling

A global exception handler provides appropriate HTTP responses.

| Situation | HTTP Status |
|---|---|
| Invalid input | `400 BAD REQUEST` |
| Invalid status transition | `400 BAD REQUEST` |
| Transaction not found | `404 NOT FOUND` |
| Duplicate Transaction ID | `409 CONFLICT` |

---

## 6. Project Structure

```text
src
├── main
│   └── java
│       └── com.example.transactionstarter
│           ├── controller
│           │   └── TransactionController.java
│           ├── dto
│           │   ├── CreateTransactionRequest.java
│           │   └── UpdateStatusRequest.java
│           ├── entity
│           │   └── Transaction.java
│           ├── exception
│           │   ├── DuplicateTransactionException.java
│           │   ├── GlobalExceptionHandler.java
│           │   └── TransactionNotFoundException.java
│           ├── repository
│           │   └── TransactionRepository.java
│           └── service
│               └── TransactionService.java
│
└── test
    └── java
        └── com.example.transactionstarter
            ├── TransactionControllerTest.java
            └── TransactionStarterApplicationTests.java
```

The main application flow is:

```text
REST Controller
       ↓
Service Layer
       ↓
Repository
       ↓
H2 Database
```

---

## 7. Testing

Automated tests were implemented using JUnit, Spring Boot Test and MockMvc.

The tests cover:

1. Successful transaction creation
2. Rejection of invalid transaction data
3. Rejection of duplicate Transaction ID
4. Request for a transaction that does not exist
5. Successful transaction status update
6. Retrieval of customer transactions

The original starter application test is also retained.

### Test Command

On Windows:

```text
.\mvnw.cmd clean test
```

On Linux/macOS:

```text
./mvnw clean test
```

### Test Result

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

---

## 8. Known Limitations

- Authentication and authorization are not implemented because they are outside
  the scope of this exercise.
- Currency values are not restricted to a predefined list because no specific
  list was provided in the assignment materials received.
- Transaction types are not restricted to a predefined list for the same reason.
- The application uses the embedded H2 database provided by the starter project.
- Pagination is not implemented for customer transaction retrieval.

---

## 9. Improvements With More Time

With more time, I would consider:

- Adding more edge-case tests.
- Adding API documentation using OpenAPI/Swagger.
- Improving the error response structure with standard error codes.
- Adding database indexes for frequently queried fields such as Customer ID.
- Adding authentication and authorization for a production environment.
- Adding additional business validation if further business requirements are
  provided.

---

## 10. Running the Project

Make sure Java 17 is available.

Run the tests using the Maven wrapper:

### Windows

```text
.\mvnw.cmd clean test
```

### Linux/macOS

```text
./mvnw clean test
```

The project uses an embedded H2 database, so no separate database installation
is required.