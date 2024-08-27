package com.softclub.trans.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.Date;

@Data
@RequiredArgsConstructor
public class SessionDTO {
    private Long id;

    private String sessionId;

    private Date lastAccessTime;
}
