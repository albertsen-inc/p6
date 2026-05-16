package com.albertsen.core.peerdiscovery;

import com.albertsen.core.utilFunctions.Logging;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReplayCache {

    private final Map<String, Long> seenID = new ConcurrentHashMap<>();
    private final Map<String, Long> seenIp = new ConcurrentHashMap<>();

    private final long WINDOW = 60_000; // 60 seconds to allow for emulator clock drift

    public boolean isReplay(String id, long timestamp, String ip) {
        long now = System.currentTimeMillis();

        if (Math.abs(now - timestamp) > WINDOW) {
            Logging.log("ReplayCache: REJECTED - Outside time window. Now: " + now + ", Msg: " + timestamp, Logging.LogLevel.warn);
            return true;
        }

        // Check if we've seen this specific message ID before
        if (seenID.putIfAbsent(id, now) != null) {
            Logging.log("ReplayCache: REJECTED - Duplicate message ID (nonce): " + id, Logging.LogLevel.info);
            return true;
        }

        // Rate-limit by IP to handle multiple network interfaces
        Long lastSeen = seenIp.get(ip);
        if (lastSeen != null && (now - lastSeen) < 500) {
            Logging.log("ReplayCache: REJECTED - IP Rate Limit (" + (now - lastSeen) + "ms since last): " + ip, Logging.LogLevel.info);
            return true;
        }
        seenIp.put(ip, now);

        Logging.log("ReplayCache: ACCEPTED - " + ip + " with ID " + id, Logging.LogLevel.info);
        cleanup(now);
        return false;
    }

    private void cleanup(long now) {
        seenID.entrySet().removeIf(e -> now - e.getValue() > WINDOW);
        seenIp.entrySet().removeIf(e -> now - e.getValue() > WINDOW);
    }
}