package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.InvoiceDTO;
import com.softclub.trans.entity.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    public Invoice toEntity(InvoiceDTO invoiceDTO);

    public InvoiceDTO toDTO(InvoiceMapper invoice);
}
