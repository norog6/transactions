package com.softclub.trans.service;

import com.softclub.trans.entity.RawData;
import com.softclub.trans.repository.RawDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RawDataService {
    @Autowired
    RawDataRepository rawDataRepository;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<RawData> readUncommittedData() {
        return rawDataRepository.findAll();
    }

}
