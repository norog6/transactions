package com.softclub.trans.DTO;

import com.softclub.trans.entity.Order;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ClientDTO {
    private Long id;

    private String name;

    private List<OrderDTO> orders;
}
