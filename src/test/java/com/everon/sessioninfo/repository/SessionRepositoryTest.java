package com.everon.sessioninfo.repository;

import com.everon.sessioninfo.domain.SessionInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import static com.everon.sessioninfo.domain.Status.FINISHED;
import static com.everon.sessioninfo.domain.Status.IN_PROGRESS;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SessionRepositoryTest {

    SessionRepository sessionRepository;

    @Before
    public void init() {
        sessionRepository = new SessionRepository();
    }

    @Test
    public void saveSessionInfo() {
        LocalDateTime startedAt = LocalDateTime.now();
        String sessionId = UUID.randomUUID().toString();
        sessionRepository.save(SessionInfo.builder().id(sessionId).stationId("ABC-1234").startedAt(startedAt).status(IN_PROGRESS).build());

        SessionInfo sessionInfo = sessionRepository.getById(sessionId);

        assertEquals(sessionId, sessionInfo.getId());
        assertEquals(IN_PROGRESS, sessionInfo.getStatus());
        assertEquals(startedAt, sessionInfo.getStartedAt());
        assertEquals("ABC-1234", sessionInfo.getStationId());
    }

    @Test
    public void updateSessionInfo() {
        LocalDateTime startedAt = LocalDateTime.now();
        LocalDateTime stoppedAt = LocalDateTime.now();
        String sessionId = UUID.randomUUID().toString();
        sessionRepository.save(SessionInfo.builder().id(sessionId).stationId("ABC-1234").startedAt(startedAt).status(IN_PROGRESS).build());
        sessionRepository.update(SessionInfo.builder().id(sessionId).stationId("ABC-1234").startedAt(startedAt).stoppedAt(stoppedAt).status(FINISHED).build());

        SessionInfo sessionInfo = sessionRepository.getById(sessionId);

        assertEquals(sessionId, sessionInfo.getId());
        assertEquals(FINISHED, sessionInfo.getStatus());
        assertEquals(startedAt, sessionInfo.getStartedAt());
        assertEquals(stoppedAt, sessionInfo.getStoppedAt());
        assertEquals("ABC-1234", sessionInfo.getStationId());
    }

    @Test
    public void getAllSessionInfo() {
        LocalDateTime startedAt = LocalDateTime.now();
        LocalDateTime stoppedAt = LocalDateTime.now();
        String sessionId1 = UUID.randomUUID().toString();
        String sessionId2 = UUID.randomUUID().toString();

        sessionRepository.save(SessionInfo.builder().id(sessionId1).stationId("ABC-1234").startedAt(startedAt).status(IN_PROGRESS).build());
        sessionRepository.save(SessionInfo.builder().id(sessionId2).stationId("ABC-1234").startedAt(startedAt).stoppedAt(stoppedAt).status(FINISHED).build());

        Collection<SessionInfo> sessionInfos = sessionRepository.getAll();
        assertEquals("Number of sessions are not as expected", 2, sessionInfos.size());

    }


}
