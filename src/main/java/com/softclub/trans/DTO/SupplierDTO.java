package com.softclub.trans.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SupplierDTO {
    private Long id;

    private String name;

    private String product;
}
