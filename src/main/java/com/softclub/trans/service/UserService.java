package com.softclub.trans.service;

import com.softclub.trans.entity.Accounts;
import com.softclub.trans.entity.User;
import com.softclub.trans.repository.AccountsRepository;
import com.softclub.trans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
    public class UserService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private TransactionTemplate transactionTemplate;

//        @Autowired
//    AccountsRepository accountsRepository;

        public void addUser(User user) {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
//                    Accounts accounts = new Accounts();
//                    accounts.setName("umpa");
//                    accounts.setBalance(2000);
//                    accountsRepository.save(accounts);
                    try {
                        userRepository.save(user);
                    } catch (Exception ex) {
                        status.setRollbackOnly();
                        throw ex;
                    }
                }
            });
        }
    }

