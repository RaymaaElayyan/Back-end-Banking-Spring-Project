package org.example.itsmybaking.service;

import org.example.itsmybaking.dto.TransactionDto;
import org.example.itsmybaking.entity.Transaction;
import org.example.itsmybaking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Component
//@Service
public  class TransactionImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

//    @Override
    public void saveTransaction(Transaction transaction) {
        // Validate the transaction details (optional validation logic)
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero.");
        }
        if (transaction.getAccountNumber() == null || transaction.getAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty.");
        }

        // Save the transaction to the database
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully. Transaction ID: " + transaction.getId());
    }

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        // Convert DTO to Entity and validate (similar logic if needed)
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();

        // Save the transaction to the database
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully. Transaction ID: " + transaction.getId());
    }
}
