package com.everon.sessioninfo.controller;

import com.everon.sessioninfo.domain.SessionInfo;
import com.everon.sessioninfo.domain.Summary;
import com.everon.sessioninfo.service.SessionStoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RequestMapping(value = "/chargingSessions")
@RestController
public class SessionStoreController {

    private final SessionStoreService sessionStoreService;

    public SessionStoreController(SessionStoreService sessionStoreService) {
        this.sessionStoreService = sessionStoreService;
    }

    @PostMapping
    public SessionInfo postSessionInfo(@RequestBody SessionInfo sessionInfo) {
        return sessionStoreService.save(sessionInfo);
    }

    @PutMapping(value = "/{id}")
    public SessionInfo updateSessionInfo(@PathVariable String id) {
        return sessionStoreService.updateSessionInfo(id);
    }

    @GetMapping
    public Collection<SessionInfo> getAllSessionInfos() {
        return sessionStoreService.getAllSessions();
    }

    @GetMapping(value = "/summary")
    public Summary getSummary() {
        return sessionStoreService.getSummary();
    }

}
