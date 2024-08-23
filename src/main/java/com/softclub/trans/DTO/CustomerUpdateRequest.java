package com.softclub.trans.DTO;

import com.softclub.trans.entity.Customer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CustomerUpdateRequest {
    private Customer customer;
    private String newAddress;

}