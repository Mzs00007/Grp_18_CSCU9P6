package vcfs.core;

/**
 * Virtual Career Fair System (VCFS)
 * Group 9 - CSCU9P6
 * Original Author: Zaid Siddiqui (Project Manager ^& Lead Developer)
 * Collaborators: Taha, YAMI, MJAMishkat, Mohamed
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_DIR = "logs";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Cached buffered writer for performance (eliminates repeated file open/close)
    private static BufferedWriter logWriter = null;
    private static String lastLogFile = null;
    private static final Object WRITER_LOCK = new Object();
    
    // Demo mode constants (visible to presenter for real-time feedback)
    public static final String VERSION = "VCFS v2.0.1 (Demo Edition)";
    public static final boolean DEMO_MODE = true;

    static {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static String getLogFilePath() {
        try {
            String today = SystemTimer.getInstance().getNow().format(DATE_FORMAT);
            return LOG_DIR + File.separator + "session_" + today + ".log";
        } catch (StackOverflowError | NullPointerException e) {
            // Fallback during SystemTimer initialization
            java.time.LocalDate today = java.time.LocalDate.now();
            return LOG_DIR + File.separator + "session_" + today + ".log";
        }
    }

    // ANSI escape codes for local terminal coloring
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BOLD_RED = "\u001B[1;31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BOLD_GREEN = "\u001B[1;32m";
    private static final String ANSI_BOLD_CYAN = "\u001B[1;36m";

    public static void log(LogLevel level, String message) {
        log(level, message, null);
    }

    public static void log(LogLevel level, String message, Throwable throwable) {
        if (message == null) {
            message = "null";
        }
        
        String timestamp = SystemTimer.getInstance().getNow().format(TIME_FORMAT);
        
        // Simplified log entry (removed expensive reflection for caller info)
        String fileLogEntry = String.format("[%s] [%s] - %s",
                timestamp, level.name(), message);
                
        // Colored log entry for the console with emoji indicators
        String colorLevel = switch (level) {
            case INFO -> ANSI_CYAN + "ℹ INFO" + ANSI_RESET;
            case WARNING -> ANSI_YELLOW + "⚠ WARNING" + ANSI_RESET;
            case ERROR -> ANSI_RED + "✗ ERROR" + ANSI_RESET;
            case CRITICAL -> ANSI_BOLD_RED + "⛔ CRITICAL" + ANSI_RESET;
        };
        
        String consoleLogEntry = String.format("[%s] [%s] - %s",
                timestamp, colorLevel, message);
                
        // Print to console for immediate feedback
        if (level == LogLevel.ERROR || level == LogLevel.CRITICAL) {
            System.err.println(consoleLogEntry);
        } else {
            System.out.println(consoleLogEntry);
        }

        if (throwable != null) {
            System.err.println(ANSI_RED + "Exception: " + throwable.getMessage() + ANSI_RESET);
        }

        // Write to log file using cached buffered writer (major performance improvement)
        synchronized (WRITER_LOCK) {
            try {
                String currentLogFile = getLogFilePath();
                
                // Check if log file changed (daily rotation)
                if (logWriter == null || !currentLogFile.equals(lastLogFile)) {
                    if (logWriter != null) {
                        logWriter.flush();
                        logWriter.close();
                    }
                    logWriter = new BufferedWriter(new FileWriter(currentLogFile, true));
                    lastLogFile = currentLogFile;
                }
                
                logWriter.write(fileLogEntry);
                logWriter.newLine();
                
                if (throwable != null) {
                    throwable.printStackTrace(new PrintWriter(logWriter));
                }
                
                // Flush periodically for ERROR/CRITICAL to ensure visibility
                if (level == LogLevel.ERROR || level == LogLevel.CRITICAL) {
                    logWriter.flush();
                }
            } catch (IOException e) {
                System.err.println("Failed to write to log file: " + e.getMessage());
            }
        }
    }

    // Convenience methods
    public static void info(String message) { log(LogLevel.INFO, message); }
    public static void warning(String message) { log(LogLevel.WARNING, message); }
    public static void error(String message) { log(LogLevel.ERROR, message); }
    public static void error(String message, Throwable t) { log(LogLevel.ERROR, message, t); }
    
    /**
     * Flush the logger buffer to ensure all entries are written to disk.
     * Called automatically for ERROR/CRITICAL levels; may be called manually for testing.
     */
    public static void flush() {
        synchronized (WRITER_LOCK) {
            if (logWriter != null) {
                try {
                    logWriter.flush();
                } catch (IOException e) {
                    System.err.println("Failed to flush log file: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Gracefully shutdown the logger (flush and close buffered writer).
     * Called automatically on JVM exit via shutdown hook.
     */
    public static void shutdown() {
        synchronized (WRITER_LOCK) {
            if (logWriter != null) {
                try {
                    logWriter.flush();
                    logWriter.close();
                    logWriter = null;
                } catch (IOException e) {
                    System.err.println("Failed to close log file: " + e.getMessage());
                }
            }
        }
    }
    
    static {
        // Register shutdown hook to ensure logs are flushed on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Logger.shutdown();
        }));
    }
    public static void critical(String message) { log(LogLevel.CRITICAL, message); }
    public static void critical(String message, Throwable t) { log(LogLevel.CRITICAL, message, t); }
}


