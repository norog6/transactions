package com.softclub.trans.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerDTO {
    private Long id;

    private Long customerId;

    private String address;
}
