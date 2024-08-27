package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.AccountsDTO;
import com.softclub.trans.entity.Accounts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    public AccountsDTO toDTO(Accounts Accounts);

    public Accounts toEntity(AccountsDTO AccountsDTO);
}
