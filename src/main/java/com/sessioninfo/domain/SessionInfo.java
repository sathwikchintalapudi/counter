package com.sessioninfo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Sathwik on 21/june/2020
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class SessionInfo {

    private String id;

    private String stationId;

    private LocalDateTime startedAt;

    private LocalDateTime stoppedAt;

    private Status status;
}
