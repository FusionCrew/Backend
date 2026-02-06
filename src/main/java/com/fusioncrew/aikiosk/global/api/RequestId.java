package com.fusioncrew.aikiosk.global.api;

import java.util.concurrent.atomic.AtomicLong;

public final class RequestId {
    private static final AtomicLong SEQ = new AtomicLong(1);

    private RequestId() {}

    public static String next() {
        return "req_" + SEQ.getAndIncrement();
    }
}