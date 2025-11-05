package org.homework.logger;

public class LoggerFactory {
    public static Logger getLogger() {
        return new Logger();
    }

    public static class Logger {
        public void debug(String message) {
            System.out.println("[DEBUG] " + message);
        }
    }
}


