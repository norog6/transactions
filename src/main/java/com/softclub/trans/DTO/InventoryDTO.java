package com.softclub.trans.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InventoryDTO {
    private Long id;

    private int quantity;
}
