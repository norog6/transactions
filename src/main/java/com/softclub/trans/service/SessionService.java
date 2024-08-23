package com.softclub.trans.service;

import com.softclub.trans.entity.Session;
import com.softclub.trans.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Transactional
    public void removeExpiredSessions(Date expirationTime) {
        List<Session> expiredSessions = sessionRepository.findExpiredSessions(expirationTime);

        sessionRepository.deleteAll(expiredSessions);
    }
}
