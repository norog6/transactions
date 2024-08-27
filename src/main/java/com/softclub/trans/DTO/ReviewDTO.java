package com.softclub.trans.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReviewDTO {
    private Long id;

    private String content;

    private double rating;
}
