package com.sessioninfo.service;

import com.sessioninfo.domain.SessionInfo;
import com.sessioninfo.domain.Status;
import com.sessioninfo.domain.Summary;
import com.sessioninfo.exceptions.SessionStoreException;
import com.sessioninfo.repository.SessionRepository;
import com.sessioninfo.utils.HitCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import static com.sessioninfo.domain.SessionStoreConstants.INVALID_SESSION_ID;
import static com.sessioninfo.domain.SessionStoreConstants.INVENTORY_CONN_ERR_CODE;

/**
 * @author Sathwik on 21/june/2020
 */
@Service
@Slf4j
public class SessionStoreService {

    private final SessionRepository sessionRepository;

    private final HitCounter startCounter, stopCounter;

    public SessionStoreService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
        this.startCounter = new HitCounter();
        this.stopCounter = new HitCounter();
    }

    /**
     * Adds the given session info to session repository by updating status, start time, assigns a unique id
     * and triggers the startCounter to log the hit
     *
     * @param sessionInfo to be saved to repository
     * @return SessionInfo that is saved to repository
     */
    public SessionInfo save(SessionInfo sessionInfo) {
        try {
            sessionInfo.setId(UUID.randomUUID().toString());
            sessionInfo.setStartedAt(LocalDateTime.now());
            sessionInfo.setStatus(Status.IN_PROGRESS);
            sessionRepository.save(sessionInfo);
            startCounter.hit(LocalDateTime.now());
            return sessionInfo;
        } catch (Exception exp) {
            log.error("Exception occurred while saving session Info", exp);
            throw new SessionStoreException("Unable to save session info", INVENTORY_CONN_ERR_CODE);
        }
    }

    /**
     * Updates session info of given id with stopTime and status "FINISHED"
     * and triggers stop counter to log the hit
     *
     * @param id the unique id assigned to a session during saving to repository
     * @return sessionInfo updated in repo with status and stop time
     */
    public SessionInfo updateSessionInfo(String id) {
        try {
            SessionInfo sessionInfo = sessionRepository.getById(id);
            if (sessionInfo == null) {
                throw new SessionStoreException("No session available with given id", INVALID_SESSION_ID);
            }
            sessionInfo.setStatus(Status.FINISHED);
            sessionInfo.setStoppedAt(LocalDateTime.now());
            sessionRepository.update(sessionInfo);
            stopCounter.hit(LocalDateTime.now());
            return sessionInfo;
        } catch (SessionStoreException sessionException) {
            log.error("Exception occurred while updating session Info", sessionException);
            throw sessionException;
        } catch (Exception exp) {
            log.error("Exception occurred while updating session Info", exp);
            throw new SessionStoreException("Unable to update session info", INVENTORY_CONN_ERR_CODE);
        }
    }

    /**
     * Gets all the stored session data from the repository
     *
     * @return a collection  of all sessions contained in repository
     */
    public Collection<SessionInfo> getAllSessions() {
        try {
            return sessionRepository.getAll();
        } catch (Exception exp) {
            log.error("Exception occurred while retrieve session Info", exp);
            throw new SessionStoreException("Unable to retrieve session info", INVENTORY_CONN_ERR_CODE);
        }
    }

    /**
     * Retrieve a summary of submitted charging sessions by accessing the startCounter and stopCounter
     * <p>
     * total number of charging session updates for the last minute
     * total number of started charging session started for the last minute
     * total number of stopped charging session  stopped for the last minute
     *
     * @return summary of charging sessions in last one minute
     */
    public Summary getSummary() {
        try {
            int startedCount, stoppedCount;
            LocalDateTime requestedDateTime = LocalDateTime.now();
            startedCount = startCounter.getHits(requestedDateTime);
            stoppedCount = stopCounter.getHits(requestedDateTime);
            return Summary.builder()
                    .startedCount(startedCount)
                    .stoppedCount(stoppedCount)
                    .totalCount(stoppedCount + startedCount)
                    .build();
        } catch (Exception exp) {
            log.error("Exception occurred while retrieve session summary", exp);
            throw new SessionStoreException("Unable to retrieve session summary", INVENTORY_CONN_ERR_CODE);
        }
    }
}
