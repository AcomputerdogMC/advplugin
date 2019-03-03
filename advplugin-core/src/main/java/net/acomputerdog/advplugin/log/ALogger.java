package net.acomputerdog.advplugin.log;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ALogger {

    public static final Level FATAL = new ALevel("FATAL", Level.SEVERE.intValue());
    public static final Level ERROR = new ALevel("ERROR", (Level.SEVERE.intValue() + Level.WARNING.intValue()) / 2);
    public static final Level WARN = Level.WARNING;
    public static final Level INFO = Level.INFO;
    public static final Level DETAIL = new ALevel("DETAIL", Level.FINE.intValue());
    public static final Level DEBUG = new ALevel("DEBUG", Level.FINER.intValue());

    private final Logger logger;

    public ALogger(Logger logger) {
        this.logger = logger;
    }

    public void logFatal(String message) {
        logger.log(FATAL, message);
    }
    public void logFatal(Supplier<String> message) {
        logger.log(FATAL, message);
    }
    public void logFatal(String message, Throwable t) {
        logger.log(FATAL, message, t);
    }
    public void logFatal(Supplier<String> message, Throwable t) {
        logger.log(FATAL, t, message);
    }

    public void logError(String message) {
        logger.log(ERROR, message);
    }
    public void logError(Supplier<String> message) {
        logger.log(ERROR, message);
    }
    public void logError(String message, Throwable t) {
        logger.log(ERROR, message, t);
    }
    public void logError(Supplier<String> message, Throwable t) {
        logger.log(ERROR, t, message);
    }

    public void logWarn(String message) {
        logger.log(WARN, message);
    }
    public void logWarn(Supplier<String> message) {
        logger.log(WARN, message);
    }
    public void logWarn(String message, Throwable t) {
        logger.log(WARN, message, t);
    }
    public void logWarn(Supplier<String> message, Throwable t) {
        logger.log(WARN, t, message);
    }

    public void logInfo(String message) {
        logger.log(INFO, message);
    }
    public void logInfo(Supplier<String> message) {
        logger.log(INFO, message);
    }
    public void logInfo(String message, Throwable t) {
        logger.log(INFO, message, t);
    }
    public void logInfo(Supplier<String> message, Throwable t) {
        logger.log(INFO, t, message);
    }

    public void logDetail(String message) {
        logger.log(DETAIL, message);
    }
    public void logDetail(Supplier<String> message) {
        logger.log(DETAIL, message);
    }
    public void logDetail(String message, Throwable t) {
        logger.log(DETAIL, message, t);
    }
    public void logDetail(Supplier<String> message, Throwable t) {
        logger.log(DETAIL, t, message);
    }

    public void logDebug(String message) {
        logger.log(DEBUG, message);
    }
    public void logDebug(Supplier<String> message) {
        logger.log(DEBUG, message);
    }
    public void logDebug(String message, Throwable t) {
        logger.log(DEBUG, message, t);
    }
    public void logDebug(Supplier<String> message, Throwable t) {
        logger.log(DEBUG, t, message);
    }

    public void setLogLevel(String level) {
        this.logger.setLevel(parseLevel(level));
    }

    private static Level parseLevel(String level) {
        switch (level.toUpperCase()) {
            case "FATAL": return FATAL;
            case "ERROR": return ERROR;
            case "WARN": return WARN;
            case "INFO": return INFO;
            case "DETAIL": return DETAIL;
            case "DEBUG": return DEBUG;
            default: return null;
        }
    }

    private static class ALevel extends Level {
        protected ALevel(String name, int value) {
            super(name, value);
        }
    }
}
