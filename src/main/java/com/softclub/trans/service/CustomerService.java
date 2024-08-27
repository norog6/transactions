package com.softclub.trans.service;

import com.softclub.trans.DTO.CustomerDTO;
import com.softclub.trans.Mapper.CustomerMapper;
import com.softclub.trans.entity.Customer;
import com.softclub.trans.entity.CustomerDetails;
import com.softclub.trans.repository.CustomerDetailsRepository;
import com.softclub.trans.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CustomerMapper customerMapper;

    @Transactional
    public void saveCustomerAndUpdateDetails(CustomerDTO customerDTO, String newAddress) {
        Customer customer = customerMapper.toEntity(customerDTO);
        customerRepository.save(customer);
        boolean updateSuccessful = Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            try {
                CustomerDetails customerDetails = customerDetailsRepository.findByCustomerId(customer.getId());
                if (customerDetails == null) {
                    customerDetails = new CustomerDetails();
                    customerDetails.setCustomerId(customer.getId());
                }
                customerDetails.setAddress(newAddress);
                customerDetailsRepository.save(customerDetails);
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                return false;
            }
        }));

        if (!updateSuccessful) {
            throw new RuntimeException("Failed to update CustomerDetails, transaction rolled back.");
        }
    }
}