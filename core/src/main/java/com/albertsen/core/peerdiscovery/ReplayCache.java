package com.albertsen.core.peerdiscovery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReplayCache {

    private final Map<String, Long> seenID = new ConcurrentHashMap<>();
    private final Map<String, Long> seenIp = new ConcurrentHashMap<>();

    private final long WINDOW = 10_000; // 10 seconds

    public boolean isReplay(String id, long timestamp, String ip) {
        long now = System.currentTimeMillis();

        if (Math.abs(now - timestamp) > WINDOW) {
            return true;
        }

        if (seenID.putIfAbsent(id, timestamp) != null) {
            return true;
        }

        if (seenIp.putIfAbsent(ip, timestamp) != null){
            return true;
        }

        cleanup(now);
        return false;
    }

    private void cleanup(long now) {
        seenID.entrySet().removeIf(e -> now - e.getValue() > WINDOW);
        seenIp.entrySet().removeIf(e -> now - e.getValue() > WINDOW);
    }
}