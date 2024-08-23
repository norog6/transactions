package com.softclub.trans.service;

import com.softclub.trans.entity.Accounts;
import com.softclub.trans.entity.User;
import com.softclub.trans.repository.AccountsRepository;
import com.softclub.trans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
    public class UserService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private TransactionTemplate transactionTemplate;



        public void addUser(User user) {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        userRepository.save(user);
                    } catch (Exception ex) {
                        status.setRollbackOnly();
                        throw ex;
                    }
                }
            });
        }
        @Transactional(propagation = Propagation.NEVER)
        public void AddUSerWithoutTransaction(User user) {
            userRepository.save(user);
        }
    }

