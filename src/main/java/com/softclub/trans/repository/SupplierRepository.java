package com.softclub.trans.repository;

import com.softclub.trans.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    void deleteByNameAndCreatedAtBefore(String name, LocalDateTime dateTime);
}
