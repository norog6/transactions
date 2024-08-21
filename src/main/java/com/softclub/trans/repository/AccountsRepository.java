package com.softclub.trans.repository;

import com.softclub.trans.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Accounts findByName(String name);
}
