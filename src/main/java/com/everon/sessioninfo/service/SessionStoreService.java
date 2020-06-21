package com.everon.sessioninfo.service;

import com.everon.sessioninfo.domain.SessionInfo;
import com.everon.sessioninfo.domain.Status;
import com.everon.sessioninfo.domain.Summary;
import com.everon.sessioninfo.repository.SessionRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
public class SessionStoreService {

    private final SessionRepo sessionRepo;

    private final HitCounter2 startCounter, stopCounter;

    public SessionStoreService(SessionRepo sessionRepo) {
        this.sessionRepo = sessionRepo;
        startCounter = new HitCounter2();
        stopCounter = new HitCounter2();
    }

    public SessionInfo save(SessionInfo sessionInfo) {
        sessionInfo.setId(UUID.randomUUID().toString());
        sessionInfo.setStartedAt(LocalDateTime.now());
        sessionInfo.setStatus(Status.IN_PROGRESS);
        sessionRepo.save(sessionInfo);
        startCounter.hit();
        return sessionInfo;
    }

    public SessionInfo updateSessionInfo(String id) {
        SessionInfo sessionInfo = sessionRepo.getById(id);
        sessionInfo.setStatus(Status.FINISHED);
        sessionInfo.setStoppedAt(LocalDateTime.now());
        sessionRepo.update(sessionInfo);
        stopCounter.hit();
        return sessionInfo;
    }


    public Collection<SessionInfo> getAllSessions() {
        return sessionRepo.getAll();
    }

    public Summary getSummary() {
        int startCount, endCount;
        startCount = startCounter.getCount(LocalDateTime.now().getHour()*60 + LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond());
        endCount = stopCounter.getCount(LocalDateTime.now().getHour()*60 + LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond());
        return Summary.builder()
                .startedCount(startCount)
                .stoppedCount(endCount)
                .totalCount(endCount + startCount)
                .build();
    }
}
