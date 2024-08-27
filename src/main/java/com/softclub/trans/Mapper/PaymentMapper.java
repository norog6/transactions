package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.PaymentDTO;
import com.softclub.trans.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    public Payment toEntity(PaymentDTO paymentDTO);

    public PaymentDTO toDTO(Payment payment);
}
