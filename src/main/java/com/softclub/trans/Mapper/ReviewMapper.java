package com.softclub.trans.Mapper;

import com.softclub.trans.DTO.ReviewDTO;
import com.softclub.trans.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    public Review toEntity(ReviewDTO reviewDTO);

    public ReviewDTO toDTO(Review review);
}
