package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.CustomerDetailsDTO;
import com.softclub.trans.entity.CustomerDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerDetailsMapper {
    public CustomerDetails toEntity(CustomerDetailsDTO customerDetailsDTO);

    public CustomerDetailsDTO toDTO(CustomerDetails customerDetails);
}
