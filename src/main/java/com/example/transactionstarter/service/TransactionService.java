package com.example.transactionstarter.service;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.exception.DuplicateTransactionException;
import com.example.transactionstarter.exception.TransactionNotFoundException;
import com.example.transactionstarter.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // A. Create transaction
    public Transaction createTransaction(CreateTransactionRequest request) {

        if (transactionRepository.existsById(request.getTransactionId())) {
            throw new DuplicateTransactionException(
                    request.getTransactionId());
        }

        Transaction transaction = new Transaction();

        transaction.setTransactionId(request.getTransactionId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency().trim().toUpperCase());
        transaction.setTransactionType(
                request.getTransactionType().trim().toUpperCase());
        transaction.setTransactionStatus(
                request.getTransactionStatus().trim().toUpperCase());

        return transactionRepository.save(transaction);
    }

    // B. Get transaction
    public Transaction getTransaction(String transactionId) {

        return transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new TransactionNotFoundException(transactionId));
    }

    // C. Update transaction status
    public Transaction updateTransactionStatus(
            String transactionId,
            String newStatus) {

        Transaction transaction = getTransaction(transactionId);

        String currentStatus =
                transaction.getTransactionStatus().toUpperCase();

        String status =
                newStatus.trim().toUpperCase();

        validateStatusTransition(currentStatus, status);

        transaction.setTransactionStatus(status);

        return transactionRepository.save(transaction);
    }

    // D. Get customer transactions
    public List<Transaction> getCustomerTransactions(String customerId) {

        return transactionRepository.findByCustomerId(customerId);
    }

    // Validate allowed status transitions
    private void validateStatusTransition(
            String currentStatus,
            String newStatus) {

        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException(
                    "Invalid transaction status: " + newStatus);
        }

        // Final statuses cannot be changed.
        if (currentStatus.equals("COMPLETED")
                || currentStatus.equals("FAILED")
                || currentStatus.equals("CANCELLED")) {

            throw new IllegalArgumentException(
                    "Status cannot be changed from " + currentStatus);
        }

        // PENDING can move to a final status.
        if (currentStatus.equals("PENDING")) {

            if (newStatus.equals("COMPLETED")
                    || newStatus.equals("FAILED")
                    || newStatus.equals("CANCELLED")) {
                return;
            }

            throw new IllegalArgumentException(
                    "Invalid status transition from PENDING to "
                            + newStatus);
        }

        throw new IllegalArgumentException(
                "Invalid current transaction status: "
                        + currentStatus);
    }

    private boolean isValidStatus(String status) {

        return status.equals("PENDING")
                || status.equals("COMPLETED")
                || status.equals("FAILED")
                || status.equals("CANCELLED");
    }
}