package com.everon.sessioninfo.service;

import com.everon.sessioninfo.utils.HitCounter;
import com.everon.sessioninfo.domain.SessionInfo;
import com.everon.sessioninfo.domain.Status;
import com.everon.sessioninfo.domain.Summary;
import com.everon.sessioninfo.exceptions.SessionStoreException;
import com.everon.sessioninfo.repository.SessionRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SessionStoreServiceTest {

    @InjectMocks
    SessionStoreService sessionStoreService;

    @Mock
    HitCounter mockStartCounter;

    @Mock
    HitCounter mockStopCounter;

    @Mock
    SessionRepository sessionRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    String sessionId;
    LocalDateTime startedAt;
    LocalDateTime stoppedAt;

    @Before
    public void init() {
        sessionId = UUID.randomUUID().toString();
        startedAt = LocalDateTime.now();
        stoppedAt = LocalDateTime.now();
        org.springframework.test.util.ReflectionTestUtils.setField(sessionStoreService, "startCounter", mockStartCounter);
        org.springframework.test.util.ReflectionTestUtils.setField(sessionStoreService, "stopCounter", mockStopCounter);
    }

    @Test
    public void save() {
        SessionInfo sessionInfo = sessionStoreService.save(SessionInfo.builder().stationId("ABC-123").build());
        assertNotNull("Session Info id is not as expected", sessionInfo.getId());
    }

    @Test
    public void save_exception() {
        expectedException.expectMessage("Unable to save session info");
        expectedException.expect(SessionStoreException.class);
        SessionInfo sessionInfo = sessionStoreService.save(null);
    }


    @Test
    public void updateSessionInfo() {
        when(sessionRepository.getById(sessionId)).thenReturn(formSessionInfo());
        SessionInfo sessionInfo = sessionStoreService.updateSessionInfo(sessionId);
        assertEquals("Session id is not as expected", sessionId, sessionInfo.getId());
        assertEquals("Session status is not as expected", Status.FINISHED, sessionInfo.getStatus());
        assertNotNull("Session stop time is not as expected", sessionInfo.getStoppedAt());
    }

    @Test
    public void updateSessionInfo_noSessionId() {
        when(sessionRepository.getById(sessionId)).thenReturn(null);
        expectedException.expectMessage("No session available with given id");
        expectedException.expect(SessionStoreException.class);
        SessionInfo sessionInfo = sessionStoreService.updateSessionInfo(sessionId);
    }

    @Test
    public void updateSessionInfo_exceptionFromRepo() {
        when(sessionRepository.getById(sessionId)).thenThrow(new NullPointerException());
        expectedException.expectMessage("Unable to update session info");
        expectedException.expect(SessionStoreException.class);
        SessionInfo sessionInfo = sessionStoreService.updateSessionInfo(sessionId);
    }

    @Test
    public void getAllSessions() {
        SessionInfo sessionInfo1 = SessionInfo.builder().id(sessionId).stationId("ABC-123").startedAt(startedAt).status(Status.IN_PROGRESS).build();
        SessionInfo sessionInfo2 = SessionInfo.builder().id(sessionId).stationId("ABC-123").startedAt(startedAt).stoppedAt(stoppedAt).status(Status.FINISHED).build();

        when(sessionRepository.getAll()).thenReturn(Arrays.asList(sessionInfo1, sessionInfo2));
        Collection<SessionInfo> sessionInfos = sessionStoreService.getAllSessions();
        assertEquals("Number of sessions are not as expected", 2, sessionInfos.size());
    }

    @Test
    public void getAllSessions_exception() {
        when(sessionRepository.getAll()).thenThrow(new NullPointerException());
        expectedException.expectMessage("Unable to retrieve session info");
        expectedException.expect(SessionStoreException.class);
        Collection<SessionInfo> sessionInfos = sessionStoreService.getAllSessions();
    }

    @Test
    public void getSummary() {
        when(mockStartCounter.getHits(any(LocalDateTime.class))).thenReturn(1);
        when(mockStopCounter.getHits(any(LocalDateTime.class))).thenReturn(1);
        Summary summary = sessionStoreService.getSummary();
        assertEquals("Number of started sessions are not as expected", 1, summary.getStartedCount());
        assertEquals("Number of stopped sessions are not as expected", 1, summary.getStoppedCount());
        assertEquals("Total number of sessions are not as expected", 2, summary.getTotalCount());
    }

    @Test
    public void getSummary_exception() {
        when(mockStartCounter.getHits(any(LocalDateTime.class))).thenThrow(new NullPointerException());
        expectedException.expectMessage("Unable to retrieve session summary");
        expectedException.expect(SessionStoreException.class);
        Summary summary = sessionStoreService.getSummary();
    }

    private SessionInfo formSessionInfo() {
        return SessionInfo.builder().id(sessionId).stationId("ABC-123").startedAt(startedAt).status(Status.IN_PROGRESS).build();
    }


}
