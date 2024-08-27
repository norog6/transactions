package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.USerProfileDTO;
import com.softclub.trans.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    public UserProfile toEntity(USerProfileDTO userProfileDTO);

    public USerProfileDTO toDTO(UserProfile userProfile);
}
