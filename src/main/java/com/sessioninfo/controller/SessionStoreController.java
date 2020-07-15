package com.sessioninfo.controller;

import com.sessioninfo.domain.SessionInfo;
import com.sessioninfo.domain.Summary;
import com.sessioninfo.service.SessionStoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


/**
 * @author Sathwik on 21/june/2020
 */
@RequestMapping(value = "/chargingSessions")
@RestController
public class SessionStoreController {

    private final SessionStoreService sessionStoreService;

    public SessionStoreController(SessionStoreService sessionStoreService) {
        this.sessionStoreService = sessionStoreService;
    }

    /**
     * Adds the given session info to session repository
     *
     * @param sessionInfo sessionInfo with stationId which need to be stored in repository
     * @return SessionInfo updated with start time, uniqueId, status
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public SessionInfo addSessionInfo(@RequestBody SessionInfo sessionInfo) {
        return sessionStoreService.save(sessionInfo);
    }

    /**
     * Updates session info of given id with stopTime and status finished
     *
     * @param id, unique Id given to session information while saving to repository
     * @return SessionInfo updated with stop time and status
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(OK)
    public SessionInfo updateSessionInfo(@PathVariable String id) {
        return sessionStoreService.updateSessionInfo(id);
    }

    /**
     * Gets all the stored session data from the repository
     *
     * @return a collection  of all sessions contained in repository
     */
    @GetMapping
    @ResponseStatus(OK)
    public Collection<SessionInfo> getAllSessionInfos() {
        return sessionStoreService.getAllSessions();
    }


    /**
     * Gets summary of submitted charging sessions
     * including
     * <p>
     * 1. total number of charging session updates for the last minute
     * 2. total number of started charging session started for the last minute
     * 3. total number of stopped charging session  stopped for the last minute
     *
     * @return summary of charging sessions in last one minute
     */
    @GetMapping(value = "/summary")
    @ResponseStatus(OK)
    public Summary getSummary() {
        return sessionStoreService.getSummary();
    }

}
