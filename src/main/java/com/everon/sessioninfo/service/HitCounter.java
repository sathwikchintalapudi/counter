package com.everon.sessioninfo.service;

import com.everon.sessioninfo.domain.Bucket;

import java.util.LinkedList;

public class HitCounter {

    private final LinkedList<Bucket> counters = new LinkedList<>();


    public void hit() {
        if (counters.size() == 0 || System.currentTimeMillis() > counters.getLast().getEndTime()) {
            counters.add(new Bucket());
        }
        Bucket bucket = counters.getLast();
        bucket.incrementCount();
    }

    public int getCount(long now) {
        int totalHits = 0;
        synchronized (this) {
            clean(now);
        }
        int size = counters.size();
        for (int i = 0; i < size; i++) {
            if (counters.get(i).getStartTime() < now) {
                totalHits = totalHits + counters.get(i).getCount();
            }
        }
        return totalHits;
    }

    private void clean(long now) {
        while (counters.size() > 0 && (counters.getFirst().getEndTime() + 60000 < now)) {
            counters.removeFirst();
        }
    }
}
