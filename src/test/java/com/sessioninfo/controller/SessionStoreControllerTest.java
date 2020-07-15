package com.sessioninfo.controller;

import com.sessioninfo.domain.SessionInfo;
import com.sessioninfo.domain.Status;
import com.sessioninfo.domain.Summary;
import com.sessioninfo.service.SessionStoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(SessionStoreController.class)
public class SessionStoreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    SessionStoreService sessionStoreService;

    private String id;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;

    @Before
    public void init() {
        id = UUID.randomUUID().toString();
        startedAt = LocalDateTime.parse("2020-01-17T12:42:04.123");
        stoppedAt = LocalDateTime.parse("2020-06-17T12:42:04.123");
    }


    @Test
    public void addSessionInfo() throws Exception {
        SessionInfo request = SessionInfo.builder().stationId("ABC-12345").build();
        SessionInfo expectedResponse = SessionInfo.builder().id(id).stationId("ABC-12345").startedAt(startedAt).status(Status.IN_PROGRESS).build();
        ObjectMapper mapper = new ObjectMapper();

        when(sessionStoreService.save(request)).thenReturn(expectedResponse);
        mvc.perform(post("/chargingSessions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("stationId").value("ABC-12345"))
                .andExpect(jsonPath("startedAt").value(startedAt.toString()))
                .andExpect(jsonPath("status").value("IN_PROGRESS"));
    }

    @Test
    public void updateSessionInfo() throws Exception {
        SessionInfo expectedResponse = SessionInfo.builder().id(id).stationId("ABC-12345").startedAt(startedAt).stoppedAt(stoppedAt).status(Status.FINISHED).build();

        when(sessionStoreService.updateSessionInfo(id)).thenReturn(expectedResponse);
        mvc.perform(put("/chargingSessions/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("stationId").value("ABC-12345"))
                .andExpect(jsonPath("startedAt").value(startedAt.toString()))
                .andExpect(jsonPath("stoppedAt").value(stoppedAt.toString()))
                .andExpect(jsonPath("status").value("FINISHED"));
    }

    @Test
    public void testGetAllSessionInfos() throws Exception {

        SessionInfo sessionInfoOne = SessionInfo.builder().id("7866d001-f1d7-4ebc-b6bf-f91f40c2c5bf").stationId("ABC-12345").startedAt(startedAt).status(Status.IN_PROGRESS).build();
        SessionInfo sessionInfoTwo = SessionInfo.builder().id("8866d001-f1d7-4ebc-b6bf-f91f40c2c5b").stationId("ABC-12346").startedAt(startedAt).stoppedAt(stoppedAt).status(Status.FINISHED).build();

        when(sessionStoreService.getAllSessions()).thenReturn(Arrays.asList(sessionInfoOne, sessionInfoTwo));

        mvc.perform(get("/chargingSessions")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("7866d001-f1d7-4ebc-b6bf-f91f40c2c5bf"))
                .andExpect(jsonPath("$.[0].stationId").value("ABC-12345"))
                .andExpect(jsonPath("$.[0].startedAt").value(startedAt.toString()))
                .andExpect(jsonPath("$.[0].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.[1].id").value("8866d001-f1d7-4ebc-b6bf-f91f40c2c5b"))
                .andExpect(jsonPath("$.[1].stationId").value("ABC-12346"))
                .andExpect(jsonPath("$.[1].startedAt").value(startedAt.toString()))
                .andExpect(jsonPath("$.[1].stoppedAt").value(stoppedAt.toString()))
                .andExpect(jsonPath("$.[1].status").value("FINISHED"));
    }

    @Test
    public void getSummary() throws Exception {
        Summary summary = Summary.builder().totalCount(2).startedCount(1).stoppedCount(1).build();
        when(sessionStoreService.getSummary()).thenReturn(summary);

        mvc.perform(get("/chargingSessions/summary")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalCount").value(2))
                .andExpect(jsonPath("startedCount").value(1))
                .andExpect(jsonPath("stoppedCount").value(1));
    }
}
