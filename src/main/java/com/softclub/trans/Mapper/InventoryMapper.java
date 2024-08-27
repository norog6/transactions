package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.InventoryDTO;
import com.softclub.trans.entity.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    public Inventory toEntity(InventoryDTO inventoryDTO);

    public InventoryDTO toDTO(Inventory inventory);
}
