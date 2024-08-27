package com.softclub.trans.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountsDTO {
    private Long id;

    private double balance;

    private String name;
}
