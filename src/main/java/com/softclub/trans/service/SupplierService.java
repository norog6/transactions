package com.softclub.trans.service;

import com.softclub.trans.DTO.SupplierDTO;
import com.softclub.trans.Mapper.SupplierMapper;
import com.softclub.trans.entity.Supplier;
import com.softclub.trans.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createAndCleanupSuppliers(SupplierDTO supplierDTO) {
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        supplier.setCreatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);

        supplierRepository.deleteByNameAndCreatedAtBefore(supplier.getName(), supplier.getCreatedAt());
    }
}
