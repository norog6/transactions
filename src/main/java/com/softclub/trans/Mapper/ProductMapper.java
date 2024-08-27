package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.ProductDTO;
import com.softclub.trans.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    public Product toEntity(ProductDTO productDTO);

    public ProductDTO toDTO(Product product);
}
