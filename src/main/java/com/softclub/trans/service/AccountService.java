package com.softclub.trans.service;

import com.softclub.trans.entity.Accounts;
import com.softclub.trans.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    @Autowired
    private AccountsRepository accountsRepository;

    @Transactional
    public void updateAccountBalance(String name, Double amount) {
        Accounts account = accountsRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт не найден"));

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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transferMoney(String fromName, String toName, double amount) {
        Accounts fromAccount = accountsRepository.findByName(fromName)
                .orElseThrow(() -> new IllegalArgumentException("Отправительский счет не найден"));

        Accounts toAccount = accountsRepository.findByName(toName)
                .orElseThrow(() -> new IllegalArgumentException("Счет получателя не найден"));

        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств на счету отправителя");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountsRepository.save(fromAccount);

        toAccount.setBalance(toAccount.getBalance() + amount);
        accountsRepository.save(toAccount);
    }
}
