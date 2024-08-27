package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.RawDataDTO;
import com.softclub.trans.entity.RawData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RawDataMapper {
    public RawData toEntity(RawDataDTO rawDataDTO);

    public RawDataDTO toDTO(RawData rawData);
}
