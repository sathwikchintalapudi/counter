package com.everon.sessioninfo.domain;

import java.time.LocalDateTime;

public class Check {

    public static void main(String[] args) {


        SessionInfo[] gg = new SessionInfo[10];
        SessionInfo sessionInfo = SessionInfo.builder().id("sathwik").build();
        gg[0] = sessionInfo;

        SessionInfo[] gg1 = gg.clone();
        System.out.println(gg1);
    }
}
