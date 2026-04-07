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
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_DIR = "logs";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static String getLogFilePath() {
        String today = SystemTimer.getInstance().getNow().format(DATE_FORMAT);
        return LOG_DIR + File.separator + "session_" + today + ".log";
    }

    // ANSI escape codes for local terminal coloring
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BOLD_RED = "\u001B[1;31m";

    public static void log(LogLevel level, String message) {
        log(level, message, null);
    }

    public static void log(LogLevel level, String message, Throwable throwable) {
        // Reflection to find who called this log method
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = null;
        
        for (int i = 2; i < stackTrace.length; i++) {
            if (!stackTrace[i].getClassName().equals(Logger.class.getName())) {
                caller = stackTrace[i];
                break;
            }
        }

        String fileName = (caller != null) ? caller.getFileName() : "Unknown";
        int lineNumber = (caller != null) ? caller.getLineNumber() : -1;
        
        String timestamp = SystemTimer.getInstance().getNow().format(TIME_FORMAT);
        
        // Plain text log entry for the file to keep it clean
        String fileLogEntry = String.format("[%s] [%s] [%s:%d] - %s",
                timestamp, level.name(), fileName, lineNumber, message);
                
        // Colored log entry for the console
        String colorLevel = switch (level) {
            case INFO -> ANSI_CYAN + "INFO" + ANSI_RESET;
            case WARNING -> ANSI_YELLOW + "WARNING" + ANSI_RESET;
            case ERROR -> ANSI_RED + "ERROR" + ANSI_RESET;
            case CRITICAL -> ANSI_BOLD_RED + "CRITICAL" + ANSI_RESET;
        };
        
        String consoleLogEntry = String.format("[%s] [%s] [%s:%d] - %s",
                timestamp, colorLevel, fileName, lineNumber, message);
                
        // Print to console for immediate feedback
        if (level == LogLevel.ERROR || level == LogLevel.CRITICAL) {
            System.err.println(consoleLogEntry);
        } else {
            System.out.println(consoleLogEntry);
        }

        if (throwable != null) {
            System.err.println(ANSI_RED + "Exception: " + throwable.getMessage() + ANSI_RESET);
        }

        // Write to log file
        try (FileWriter fw = new FileWriter(getLogFilePath(), true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(fileLogEntry);
            if (throwable != null) {
                throwable.printStackTrace(pw);
            }
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    // Convenience methods
    public static void info(String message) { log(LogLevel.INFO, message); }
    public static void warning(String message) { log(LogLevel.WARNING, message); }
    public static void error(String message) { log(LogLevel.ERROR, message); }
    public static void error(String message, Throwable t) { log(LogLevel.ERROR, message, t); }
    public static void critical(String message) { log(LogLevel.CRITICAL, message); }
    public static void critical(String message, Throwable t) { log(LogLevel.CRITICAL, message, t); }
}


