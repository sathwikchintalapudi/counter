package com.everon.sessioninfo.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class HitCounterTest {

    private HitCounter hitCounter;

    @Before
    public void init() {
        hitCounter = new HitCounter();
    }

    @Test
    public void hit() {
        hitCounter.hit(LocalDateTime.of(2020, 6, 22, 1, 1, 1));
        hitCounter.hit(LocalDateTime.of(2020, 6, 22, 1, 1, 1));

        assertEquals("Hit counter value is not as expected", 2, hitCounter.getHits(LocalDateTime.of(2020, 6, 22, 1, 1, 5)));
    }

    @Test
    public void hit_verifyMultipleRequestsInASec() {
        hitCounter.hit(LocalDateTime.of(2020, 6, 22, 1, 1, 1));
        hitCounter.hit(LocalDateTime.of(2020, 6, 22, 1, 2, 1));

        assertEquals("Hit counter value is not as expected", 1, hitCounter.getHits(LocalDateTime.of(2020, 6, 22, 1, 3, 0)));
    }

    @Test
    public void hit_verifyExpiry() {
        hitCounter.hit(LocalDateTime.of(2020, 6, 22, 1, 1, 1));
        hitCounter.hit(LocalDateTime.of(2020, 6, 22, 1, 2, 1));
        hitCounter.hit(LocalDateTime.of(2020, 6, 22, 1, 2, 5));

        assertEquals("Hit counter value is not as expected", 1, hitCounter.getHits(LocalDateTime.of(2020, 6, 22, 1, 3, 5)));
        assertEquals("Hit counter value is not as expected", 0, hitCounter.getHits(LocalDateTime.of(2020, 6, 22, 1, 3, 6)));
    }

    @Test
    public void hit_zeroMinute_dayChange_scenario() {
        hitCounter.hit(LocalDateTime.of(2020, 6, 22, 23, 59, 59));
        hitCounter.hit(LocalDateTime.of(2020, 6, 23, 0, 0, 0));
        hitCounter.hit(LocalDateTime.of(2020, 6, 23, 0, 0, 1));

        assertEquals("Hit counter value is not as expected", 3, hitCounter.getHits(LocalDateTime.of(2020, 6, 23, 0, 0, 5)));
        assertEquals("Hit counter value is not as expected", 3, hitCounter.getHits(LocalDateTime.of(2020, 6, 23, 0, 0, 6)));
    }


    private int getTimeStamp(LocalDateTime timeStamp) {
        return timeStamp.getHour() * 60 * 60 + timeStamp.getMinute() * 60 + timeStamp.getSecond();
    }
}
