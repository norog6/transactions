package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.UserDTO;
import com.softclub.trans.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User toEntity(UserDTO userDTO);

    public UserDTO toDTO(User user);
}
