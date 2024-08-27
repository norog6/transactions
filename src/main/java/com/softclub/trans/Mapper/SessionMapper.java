package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.SessionDTO;
import com.softclub.trans.entity.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    public Session toEntity(SessionDTO sessionDTO);

    public SessionDTO toDTO(Session session);
}
