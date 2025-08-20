package org.example.codesignconnect.utils;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebsocketUtils {
    private static final Map<String, Map<String, Session>> onlineUsersByPath = new ConcurrentHashMap<>();

    public static void onUserConnected(String path, String user, Session session) {
        onlineUsersByPath.computeIfAbsent(path, k -> new ConcurrentHashMap<>()).put(user, session);
        log.info("User {} of {}: online", user, path);
    }

    public static void onUserDisconnected(String path, String user) {
        Map<String, Session> sessions = onlineUsersByPath.get(path);
        if (sessions != null) {
            sessions.remove(user);
            log.info("User {} of {}: offline", user, path);
        }
    }

    public static void broadcast(String path, String message) {
        Map<String, Session> sessions = onlineUsersByPath.get(path);
        if (sessions != null) {
            sessions.values().forEach(session -> {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    printErrorMsg(e);
                }
            });
        }
    }

    public static Set<String> getOnlineUsers(String path) {
        Map<String, Session> sessions = onlineUsersByPath.get(path);
        return sessions != null ? sessions.keySet() : Collections.emptySet();
    }

    public static Session getSession(String path, String user) {
        Map<String, Session> sessions = onlineUsersByPath.get(path);
        return sessions != null ? sessions.get(user) : null;
    }

    public static void printErrorMsg(Exception e) {
        log.error("Error: {}", e.getMessage());
    }
}
