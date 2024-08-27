package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.OrderDTO;
import com.softclub.trans.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    public Order toEntity(OrderDTO orderDto);

    public OrderDTO toDTO(Order order);
}
