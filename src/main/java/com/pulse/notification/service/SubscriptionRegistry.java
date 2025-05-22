package com.pulse.notification.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SubscriptionRegistry {

    private static final Map<String, String> sessions = new ConcurrentHashMap<>();

    public static void add(String sessionId, String destination) {
        sessions.put(sessionId, destination);
    }

    public static String getDestination(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void remove(String sessionId) {
        sessions.remove(sessionId);
    }
}
