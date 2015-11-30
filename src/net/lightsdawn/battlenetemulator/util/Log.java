package net.lightsdawn.battlenetemulator.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

public final class Log {
    private static final PrintStream out = System.out;
    private static PrintWriter writer = null;
    private static boolean debugLogging = true;
    private static boolean fileLogging = true;
    private static final DateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss]");
    private static final String prefix = " [INFO] ";
    private static final String errorPrefix = " [ERROR] ";
    private static final Object lockObject = new Object();

    public static final boolean initialise() {
        try {
            synchronized (lockObject) {
                writer = new PrintWriter("log.txt", "UTF-8");
                return true;
            }
        } catch (Exception e) {
            debugLogging = false;
            log("Error trying to initialise logger: " + e.getMessage(), true);
        } catch (Throwable t) {
            debugLogging = false;
            log("Error trying to initalise logger:  " + t.getMessage(), true);
        }
        return false;
    }

    public static final void close() {
        try {
            synchronized (lockObject) {
                if (writer != null)
                    writer.close();
            }
        } finally {
        }
    }

    private Log() {
        throw new AssertionError("This class cannot be instantiated.");
    }

    public static final void log(String message, boolean error, Object... args) {
        synchronized (lockObject) {
            String output = getFormattedMessage(message, error, args);
            if (fileLogging)
                writer.write(output + System.lineSeparator());
            out.println(output);
        }
    }

    public static final void debug(String message, boolean error, Object... args) {
        if (debugLogging)
            log(message, error, args);
    }

    private static final String getFormattedMessage(String message, boolean error, Object... args) {
        String returnVal = dateFormat.format(new Date());
        returnVal += error ? errorPrefix : prefix;
        return returnVal + String.format(message, args);
    }
}
