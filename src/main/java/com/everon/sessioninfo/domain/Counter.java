package com.everon.sessioninfo.domain;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter {

    public final LinkedList<Bucket1> bucket1s = new LinkedList<>();

    public Counter(long minute, AtomicInteger atomicInteger) {
        Bucket1 bucket1 = new Bucket1(minute, atomicInteger);
        bucket1s.add(bucket1);
    }

    public void addBucket(long minute) {
        if (bucket1s.getLast().getMinute() == minute) {
            bucket1s.getLast().incrementHitCount();
        } else if (bucket1s.size() == 2) {
            bucket1s.add(new Bucket1(minute, new AtomicInteger(1)));
            bucket1s.removeFirst();
        } else {
            bucket1s.add(new Bucket1(minute, new AtomicInteger(1)));
        }
    }

    public int getCount(long minute) {
        int hits = 0;
        if (bucket1s.getFirst().getMinute() == minute) {
            hits = bucket1s.getFirst().getHits();
        } else if (bucket1s.getLast().getMinute() == minute) {
            hits = bucket1s.getLast().getHits();
        }
        return hits;
    }

}
