package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.CustomerDTO;
import com.softclub.trans.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    public CustomerDTO toDTO(Customer customer);

    public Customer toEntity(CustomerDTO customerDto);

}
