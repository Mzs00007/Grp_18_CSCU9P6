package vcfs.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SESSION MANAGER - Tracks active user sessions and live portal synchronization
 * 
 * Features:
 * - Real-time session tracking
 * - Portal activity monitoring
 * - Live data synchronization across portals
 * - Session recovery on crash
 * - User action audit trail
 */
public class SessionManager {
    
    private static SessionManager instance;
    private final Map<String, UserSession> activeSessions = new ConcurrentHashMap<>();
    private final List<PortalActivity> activityLog = new CopyOnWriteArrayList<>();
    private volatile boolean isLiveMode = false;
    
    private SessionManager() {
        Logger.info("[SessionManager] Initialized - tracking live portal usage");
    }
    
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Register a user session
     */
    public void startSession(String sessionId, String userName, String roleType) {
        UserSession session = new UserSession(sessionId, userName, roleType, System.currentTimeMillis());
        activeSessions.put(sessionId, session);
        
        recordActivity(userName, roleType, "SESSION_START", "Logged in to " + roleType + " portal");
        Logger.info("[SessionManager] " + userName + " started " + roleType + " session");
    }
    
    /**
     * End a user session
     */
    public void endSession(String sessionId) {
        UserSession session = activeSessions.remove(sessionId);
        if (session != null) {
            recordActivity(session.userName, session.roleType, "SESSION_END", "Logged out");
            Logger.info("[SessionManager] " + session.userName + " ended session");
        }
    }
    
    /**
     * Record a portal activity
     */
    public void recordActivity(String userName, String portal, String actionType, String details) {
        PortalActivity activity = new PortalActivity(
            System.currentTimeMillis(),
            userName,
            portal,
            actionType,
            details
        );
        activityLog.add(activity);
        
        // Keep only last 500 activities for memory
        if (activityLog.size() > 500) {
            activityLog.remove(0);
        }
    }
    
    /**
     * Get all active sessions
     */
    public List<String> getActiveSessions() {
        List<String> sessions = new ArrayList<>();
        for (UserSession session : activeSessions.values()) {
            long duration = System.currentTimeMillis() - session.startTime;
            sessions.add(String.format(
                "%s (%s) - %dms active",
                session.userName, session.roleType, duration
            ));
        }
        return sessions;
    }
    
    /**
     * Get activity in a specific portal
     */
    public List<String> getPortalActivity(String portalType, int limit) {
        List<String> activities = new ArrayList<>();
        int count = 0;
        
        // Get last 'limit' activities from this portal
        for (int i = activityLog.size() - 1; i >= 0 && count < limit; i--) {
            PortalActivity activity = activityLog.get(i);
            if (activity.portal.equalsIgnoreCase(portalType)) {
                activities.add(0, String.format(
                    "[%s] %s: %s - %s",
                    formatTime(activity.timestamp),
                    activity.actionType,
                    activity.userName,
                    activity.details
                ));
                count++;
            }
        }
        
        return activities;
    }
    
    /**
     * Get all recent activity
     */
    public List<String> getRecentActivity(int limit) {
        List<String> activities = new ArrayList<>();
        int start = Math.max(0, activityLog.size() - limit);
        
        for (int i = start; i < activityLog.size(); i++) {
            PortalActivity activity = activityLog.get(i);
            activities.add(String.format(
                "[%s] [%s] %s: %s - %s",
                formatTime(activity.timestamp),
                activity.portal,
                activity.actionType,
                activity.userName,
                activity.details
            ));
        }
        
        return activities;
    }
    
    /**
     * Enable live mode for multi-portal demonstrations
     */
    public void enableLiveMode() {
        isLiveMode = true;
        Logger.info("[SessionManager] LIVE MODE ENABLED - Multi-portal synchronization active");
    }
    
    /**
     * Check if system is in live demo mode
     */
    public boolean isLiveMode() {
        return isLiveMode;
    }
    
    /**
     * Get session count
     */
    public int getActiveSessionCount() {
        return activeSessions.size();
    }
    
    private String formatTime(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        return sdf.format(new java.util.Date(timestamp));
    }
    
    /**
     * Inner class: User Session
     */
    public static class UserSession {
        public final String sessionId;
        public final String userName;
        public final String roleType;
        public final long startTime;
        
        public UserSession(String sessionId, String userName, String roleType, long startTime) {
            this.sessionId = sessionId;
            this.userName = userName;
            this.roleType = roleType;
            this.startTime = startTime;
        }
    }
    
    /**
     * Inner class: Portal Activity
     */
    public static class PortalActivity {
        public final long timestamp;
        public final String userName;
        public final String portal;
        public final String actionType;
        public final String details;
        
        public PortalActivity(long timestamp, String userName, String portal, 
                             String actionType, String details) {
            this.timestamp = timestamp;
            this.userName = userName;
            this.portal = portal;
            this.actionType = actionType;
            this.details = details;
        }
    }
}
