package org.example.itsmybaking.service;

import org.example.itsmybaking.dto.TransactionDto;
import org.example.itsmybaking.entity.Transaction;

public interface TransactionService {
        void saveTransaction(TransactionDto transactionDto);
}
