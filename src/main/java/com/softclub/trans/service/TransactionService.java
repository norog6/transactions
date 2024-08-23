package com.softclub.trans.service;

import com.softclub.trans.entity.Transaction;
import com.softclub.trans.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private static final double TRANSACTION_LIMIT = 1000.00;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void addTransaction(Transaction transaction) {
        if (transaction.getAmount() > TRANSACTION_LIMIT) {
            throw new IllegalArgumentException("Transaction amount exceeds the limit. Transaction rolled back.");
        }
        transactionRepository.save(transaction);
    }
}