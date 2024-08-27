package com.softclub.trans.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class USerProfileDTO {
    private Long id;

    private String name;

    private String email;

    private String gender;

}
