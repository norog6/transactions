package com.softclub.trans.service;

import com.softclub.trans.entity.Inventory;
import com.softclub.trans.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateInventory(Long productId, int newStock) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        inventory.setQuantity(inventory.getQuantity() + newStock);

        inventoryRepository.save(inventory);
    }
}
