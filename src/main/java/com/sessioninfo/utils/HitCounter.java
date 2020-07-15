package com.sessioninfo.utils;

import com.sessioninfo.functionalinterface.TriPredicate;
import com.sessioninfo.domain.SessionStoreConstants;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Sathwik on 21/june/2020
 */
@Slf4j
public class HitCounter {

    private final int[] timeRecorder = new int[SessionStoreConstants.TOTAL_SECONDS_IN_MINUTE];

    private final int[] hitRecorder = new int[SessionStoreConstants.TOTAL_SECONDS_IN_MINUTE];

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock writeLock = lock.writeLock();
    private final Lock readLock = lock.readLock();

    /**
     * Records a hit to timeRecorder, hitRecorder basing on the time stamp
     * <p>
     * Example :
     * If a hit method is called at 22-06-2020 12:01:01
     * Step -1 :
     * then time stamp will be calculated with formula : currentHours * no.of min hour * no.of sec in a min + current min * 60 + no.of current seconds at that movement
     * according to it 22-06-2020 12:01:01 will give 12 * 60 * 60 + 1 * 60 + 1 is the time stamp
     * Step -2:
     * Using this time stamp array index will be calculated using which time stamp and hit number will be placed in timeRecorder, hitRecorder
     * in our example 43261 % 60 = 1. here 1 is the index where we store the time stamp(43261) and number of hit info in timeRecorder, hitRecorder
     * <p>
     * Below method is thread safe i.e only one thread is allowed to record its hit in timeRecorder, hitRecorder at a time
     *
     * @param dateTime current date-time when hit occurred
     */
    public void hit(LocalDateTime dateTime) {
        try {
            int timeStamp = getTimeStamp(dateTime);
            int index = timeStamp % SessionStoreConstants.TOTAL_SECONDS_IN_MINUTE;
            writeLock.lock();
            try {
                if (timeRecorder[index] != timeStamp) {
                    timeRecorder[index] = timeStamp;
                    hitRecorder[index] = 1;
                } else {
                    hitRecorder[index]++;
                }
            } finally {
                writeLock.unlock();
            }
        } catch (Exception exp) {
            log.error("Exception occurred while recording a hit", exp);
        }
    }

    /**
     * It calculates the number of hits occurred in the duration of last 60 seconds or 1 min from the summary requested
     *
     * @param dateTime time at which summary of session info of last 1 minute is requested
     * @return gives info about number of hits occurred in last 60 seconds or 1 min
     */
    public int getHits(LocalDateTime dateTime) {
        int currentTimeStamp = getTimeStamp(dateTime);
        int upperLimit = currentTimeStamp;
        int lowerLimit = getLowerLimit(currentTimeStamp);
        TriPredicate triPredicate = (upperLimit > lowerLimit) ?
                (timeStampInfo, upperLimitInfo, lowerLimitInfo) -> (timeStampInfo >= lowerLimitInfo && timeStampInfo <= upperLimitInfo) :
                (timeStampInfo, upperLimitInfo, lowerLimitInfo) -> (timeStampInfo >= lowerLimitInfo && timeStampInfo <= SessionStoreConstants.TOTAL_SECONDS_IN_DAY || timeStampInfo >= 0 && timeStampInfo <= upperLimitInfo);
        int count = 0;

        readLock.lock();
        try {
            for (int i = 0; i <= 59; i++) {
                int timeStamp = timeRecorder[i];
                if (triPredicate.test(timeStamp, upperLimit, lowerLimit)) {
                    count = count + hitRecorder[i];
                }
            }
        } finally {
            readLock.unlock();
        }
        return count;
    }

    /**
     * Gets the lower limit of the 60 seconds from where the count need to be calculated
     * <p>
     * Example:
     * case 1 - Summary request came at 1170 seconds then upper limit will be 1170 and *lowe limit* will be 1110
     * case 2 - Summary request came at 10 seconds then upper limit will be 10 and *lowe limit* will be 86350
     * Case 2 scenario day has just changed so part of minute is present in the previous day
     * and part in current day so lower limit will be present in the previous day
     *
     * @param timeStamp in seconds
     * @return time(in seconds) from which count need to be calculated
     */
    private int getLowerLimit(int timeStamp) {
        int lowerLimit = timeStamp - SessionStoreConstants.TOTAL_SECONDS_IN_MINUTE;
        if (lowerLimit < 0) {
            return SessionStoreConstants.TOTAL_SECONDS_IN_DAY + lowerLimit;
        }
        return lowerLimit;
    }

    private int getTimeStamp(LocalDateTime dateTime) {
        return dateTime.getHour() * SessionStoreConstants.TOTAL_MINUTES_IN_HOUR * SessionStoreConstants.TOTAL_SECONDS_IN_MINUTE + dateTime.getMinute() * SessionStoreConstants.TOTAL_SECONDS_IN_MINUTE + dateTime.getSecond();
    }
}
