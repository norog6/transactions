package com.softclub.trans.DTO;

import com.softclub.trans.entity.Invoice;
import com.softclub.trans.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class OrderInvoice {
    private Order order;
    private Invoice invoice;
}
