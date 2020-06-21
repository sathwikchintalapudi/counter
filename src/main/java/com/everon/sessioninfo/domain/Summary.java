package com.everon.sessioninfo.domain;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Summary {
    private int totalCount;
    private int startedCount;
    private int stoppedCount;
}
