package com.softclub.trans.service;

import com.softclub.trans.entity.Accounts;
import com.softclub.trans.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    @Autowired
    private AccountsRepository accountsRepository;

    @Transactional
    public void updateAccountBalance(String name, Double amount) {
        Accounts account = accountsRepository.findByName(name);

        if (account == null) {
            throw new RuntimeException("Account not found for user: " + name);
        }

        double newBalance = account.getBalance() + amount;

        if (newBalance < 0) {
            throw new RuntimeException("Balance<0");
        }

        account.setBalance(newBalance);
        accountsRepository.save(account);
    }
}
