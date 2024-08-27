package com.softclub.trans.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InvoiceDTO {
    private Long id;

    private Long orderId;

    private Double amount;
}
