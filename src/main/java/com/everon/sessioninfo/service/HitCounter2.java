package com.everon.sessioninfo.service;

import com.everon.sessioninfo.domain.Counter;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class HitCounter2 {

    private final Counter[] hitCounter = new Counter[60];

    public synchronized void hit() {
        LocalDateTime hitTime = LocalDateTime.now();
        if (hitCounter[hitTime.getSecond()] == null) {
            long minute = hitTime.getHour() * 60 + hitTime.getMinute();
            Counter counter = new Counter(minute, new AtomicInteger(1));
            hitCounter[hitTime.getSecond()] = counter;
        } else {
            long minute = hitTime.getHour() * 60 + hitTime.getMinute();
            hitCounter[hitTime.getSecond()].addBucket(minute);
        }
    }

    public int getCount(long minute, int second) {
        int value = 0;
        for (int i = second; i <= 59; i++) {
            if (hitCounter[i] != null) {
                value = value + hitCounter[i].getCount(minute - 1 >= 0 ? minute - 1 : 1440);
            }
        }
        for (int i = second - 1; i >= 0; i--) {
            if (hitCounter[i] != null) {
                value = value + hitCounter[i].getCount(minute);
            }
        }
        return value;
    }
}
