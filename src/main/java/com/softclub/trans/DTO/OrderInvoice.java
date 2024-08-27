package com.softclub.trans.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class OrderInvoice {
    private OrderDTO orderDTO;

    private InvoiceDTO invoiceDTO;
}
