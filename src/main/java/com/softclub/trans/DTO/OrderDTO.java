package com.softclub.trans.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderDTO {
    private Long id;

    private String description;

    private double amount;

}
