package com.softclub.trans.repository;

import com.softclub.trans.entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Long> {
    CustomerDetails findByCustomerId(Long customerId);
}

