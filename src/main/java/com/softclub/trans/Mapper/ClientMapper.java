package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.ClientDTO;
import com.softclub.trans.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    public ClientDTO toDTO(Client client);

    public Client toEntity(ClientDTO clientDTO);
}
