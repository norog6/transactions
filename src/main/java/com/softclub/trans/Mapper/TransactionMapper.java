package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.TransactionDTO;
import com.softclub.trans.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    public Transaction toEntity(TransactionDTO transactionDTO);

    public TransactionDTO toDTO(Transaction transaction);
}
