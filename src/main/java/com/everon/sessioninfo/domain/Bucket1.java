package com.everon.sessioninfo.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class Bucket1 {


    private long minute;

    private AtomicInteger hits;

    public Bucket1(long minute, AtomicInteger hits) {
        this.minute = minute;
        this.hits = hits;
    }

    public long getMinute() {
        return minute;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public int getHits() {
        return hits.intValue();
    }

    public void setHits(AtomicInteger hits) {
        this.hits = hits;
    }

    public void incrementHitCount() {
        hits.incrementAndGet();
    }


}
