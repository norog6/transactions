package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.SupplierDTO;
import com.softclub.trans.entity.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    public Supplier toEntity(SupplierDTO supplierDTO);

    public SupplierDTO toDTO(Supplier supplier);
}
