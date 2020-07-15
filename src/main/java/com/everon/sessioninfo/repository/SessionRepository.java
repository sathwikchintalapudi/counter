package com.everon.sessioninfo.repository;

import com.everon.sessioninfo.domain.SessionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sathwik on 21/june/2020
 */
@Component
@Slf4j
public class SessionRepository {

    private final Map<String, SessionInfo> sessionStore = new ConcurrentHashMap<>();

    /**
     * Saves the session info to memory
     *
     * @param sessionInfo session to be stored in memory
     */
    public void save(SessionInfo sessionInfo) {
        sessionStore.put(sessionInfo.getId(), sessionInfo);
    }

    /**
     * Updates the Session info to memory
     *
     * @param sessionInfo to be updated in memory
     */
    public void update(SessionInfo sessionInfo) {
        sessionStore.replace(sessionInfo.getId(), sessionInfo);
    }

    /**
     * Gets the session info by Id from memory
     *
     * @param id unique id of the session to be retrieved from the session
     * @return sessionInfo retrieved from the the memory
     */
    public SessionInfo getById(String id) {
        return sessionStore.get(id);
    }

    /**
     * Retrieves all the session infos from the memory
     *
     * @return all the session info from the memory
     */
    public Collection<SessionInfo> getAll() {
        return sessionStore.values();
    }

}
