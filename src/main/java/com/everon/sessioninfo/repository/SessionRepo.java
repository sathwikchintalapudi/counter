package com.everon.sessioninfo.repository;

import com.everon.sessioninfo.domain.SessionInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRepo {

    private final Map<String, SessionInfo> sessionInventory = new ConcurrentHashMap<>();

    public void save(SessionInfo sessionInfo) {
        sessionInventory.put(sessionInfo.getId(), sessionInfo);
    }

    public void update(SessionInfo sessionInfo) {
        sessionInventory.replace(sessionInfo.getId(), sessionInfo);
    }

    public SessionInfo getById(String id) {
        return sessionInventory.get(id);
    }

    public Collection<SessionInfo> getAll() {
        return sessionInventory.values();
    }

}
