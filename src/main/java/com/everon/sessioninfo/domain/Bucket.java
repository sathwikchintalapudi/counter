package com.everon.sessioninfo.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class Bucket {

    private AtomicInteger count;

    private final Long startTime;

    private final Long endTime;

    public Bucket() {
        this.count = new AtomicInteger(0);
        this.startTime = System.currentTimeMillis();
        this.endTime = System.currentTimeMillis() + 1000L;
    }

    public int getCount() {
        return count.intValue();
    }

    public Long getStartTime() {
        return startTime;
    }

    public void incrementCount() {
        this.count.incrementAndGet();
    }

    public Long getEndTime() {
        return endTime;
    }
}
