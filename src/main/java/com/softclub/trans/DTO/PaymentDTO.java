package com.softclub.trans.DTO;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentDTO {
    private Long id;

    @Column
    private Long orderId;

    @Column
    private Double amount;
}
