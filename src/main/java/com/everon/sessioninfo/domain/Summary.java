package com.everon.sessioninfo.domain;


import lombok.Builder;
import lombok.Data;

/**
 * @author Sathwik on 21/june/2020
 */
@Data
@Builder
public class Summary {
    private int totalCount;
    private int startedCount;
    private int stoppedCount;
}
