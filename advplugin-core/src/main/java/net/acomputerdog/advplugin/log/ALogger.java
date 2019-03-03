package net.acomputerdog.advplugin.log;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ALogger {
    private final Logger logger;

    private ALevel minimumLevel = ALevel.INFO;

    public ALogger(Logger logger) {
        this.logger = logger;
    }

    private void writeLog(ALevel level, String message, Throwable t) {
        message = "[" + level.name() + "] " + message;

        if (t != null) {
            this.logger.log(level.passthrough, message, t);
        } else {
            this.logger.log(level.passthrough, message);
        }
    }
    
    public void log(ALevel level, String message) {
        if (level.level >= minimumLevel.level) {
            this.writeLog(level, message, null);
        }
    }
    
    public void log(ALevel level, Supplier<String> message) {
        if (level.level >= minimumLevel.level) {
            this.writeLog(level, message.get(), null);
        }
    }
    
    public void log(ALevel level, String message, Throwable t) {
        if (level.level >= minimumLevel.level) {
            this.writeLog(level, message, t);
        }
    }

    public void log(ALevel level, Supplier<String> message, Throwable t) {
        if (level.level >= minimumLevel.level) {
            this.writeLog(level, message.get(), t);
        }
    }
    
    public void logFatal(String message) {
        this.log(ALevel.FATAL, message);
    }
    public void logFatal(Supplier<String> message) {
        this.log(ALevel.FATAL, message);
    }
    public void logFatal(String message, Throwable t) {
        this.log(ALevel.FATAL, message, t);
    }
    public void logFatal(Supplier<String> message, Throwable t) {
        this.log(ALevel.FATAL, message, t);
    }

    public void logError(String message) {
        this.log(ALevel.ERROR, message);
    }
    public void logError(Supplier<String> message) {
        this.log(ALevel.ERROR, message);
    }
    public void logError(String message, Throwable t) {
        this.log(ALevel.ERROR, message, t);
    }
    public void logError(Supplier<String> message, Throwable t) {
        this.log(ALevel.ERROR, message, t);
    }

    public void logWarn(String message) {
        this.log(ALevel.WARN, message);
    }
    public void logWarn(Supplier<String> message) {
        this.log(ALevel.WARN, message);
    }
    public void logWarn(String message, Throwable t) {
        this.log(ALevel.WARN, message, t);
    }
    public void logWarn(Supplier<String> message, Throwable t) {
        this.log(ALevel.WARN, message, t);
    }

    public void logInfo(String message) {
        this.log(ALevel.INFO, message);
    }
    public void logInfo(Supplier<String> message) {
        this.log(ALevel.INFO, message);
    }
    public void logInfo(String message, Throwable t) {
        this.log(ALevel.INFO, message, t);
    }
    public void logInfo(Supplier<String> message, Throwable t) {
        this.log(ALevel.INFO, message, t);
    }

    public void logDetail(String message) {
        this.log(ALevel.DETAIL, message);
    }
    public void logDetail(Supplier<String> message) {
        this.log(ALevel.DETAIL, message);
    }
    public void logDetail(String message, Throwable t) {
        this.log(ALevel.DETAIL, message, t);
    }
    public void logDetail(Supplier<String> message, Throwable t) {
        this.log(ALevel.DETAIL, message, t);
    }

    public void logDebug(String message) {
        this.log(ALevel.DEBUG, message);
    }
    public void logDebug(Supplier<String> message) {
        this.log(ALevel.DEBUG, message);
    }
    public void logDebug(String message, Throwable t) {
        this.log(ALevel.DEBUG, message, t);
    }
    public void logDebug(Supplier<String> message, Throwable t) {
        this.log(ALevel.DEBUG, message, t);
    }

    public void setLogLevel(ALevel level) {
        if (level != null) {
            this.minimumLevel = level;
        }
    }

    public ALevel getMinimumLevel() {
        return minimumLevel;
    }

    public void setMinimumLevel(ALevel minimumLevel) {
        this.minimumLevel = minimumLevel;
    }

    public enum ALevel {
        FATAL(100, Level.SEVERE),
        ERROR(90, Level.SEVERE),
        WARN(70, Level.WARNING),
        INFO(50, Level.INFO),
        DETAIL(40, Level.INFO),
        DEBUG(20, Level.INFO);
        
        private final int level;
        private final Level passthrough;

        ALevel(int level, Level passthrough) {
            this.level = level;
            this.passthrough = passthrough;
        }

        public int getLevel() {
            return level;
        }

        public Level getPassthrough() {
            return passthrough;
        }

        public static ALevel parse(String level) {
            switch (level.toUpperCase()) {
                case "FATAL": return ALevel.FATAL;
                case "ERROR": return ALevel.ERROR;
                case "WARN": return ALevel.WARN;
                case "INFO": return ALevel.INFO;
                case "DETAIL": return ALevel.DETAIL;
                case "DEBUG": return ALevel.DEBUG;
                default: throw new IllegalArgumentException("Unknown level: '" + level + "'");
            }
        }
    }
}
