package fr.arinonia.bootstrap.logger;

import fr.arinonia.bootstrap.file.FileManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class Logger {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static java.util.logging.Logger logger;
    private static FileHandler fileHandler;
    private static Path logFile;

    public static void init(final FileManager fileManager) {
        try {
            final Path logsDir = fileManager.getRootPath().resolve("logs");
            Files.createDirectories(logsDir);

            final String fileName = "bootstrap_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".log";
            logFile = logsDir.resolve(fileName);

            logger = java.util.logging.Logger.getLogger("AltisCore");
            logger.setUseParentHandlers(false);

            fileHandler = new FileHandler(logFile.toString());
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(final LogRecord record) {
                    return String.format("[%s] [%s] [%s] %s%n",
                            DATE_FORMAT.format(new Date(record.getMillis())),
                            record.getLevel().getName(),
                            record.getSourceClassName(),
                            record.getMessage()
                    );
                }
            });

            final ConsoleHandler consoleHandler = new ConsoleHandler() {
                // multiple threads can write to System.out, so we need to synchronize
                @Override
                protected synchronized void setOutputStream(final OutputStream out) throws SecurityException {
                    super.setOutputStream(System.out);
                }
            };
            consoleHandler.setFormatter(new Formatter() {
                @Override
                public String format(final LogRecord record) {
                    return String.format("[%s] [%s] %s%n",
                            DATE_FORMAT.format(new Date(record.getMillis())),
                            record.getLevel().getName(),
                            record.getMessage()
                    );
                }
            });

            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);

            logger.setLevel(Level.ALL);

            info("Logger initialized - Log file: " + logFile);
        } catch (final IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void info(final String message) {
        logger.info(message);
    }

    public static void warning(final String message) {
        logger.warning(message);
    }

    public static void error(final String message) {
        logger.severe(message);
    }

    public static void error(final String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }

    public static void debug(final String message) {
        logger.fine(message);
    }

    public static Path getLogFile() {
        return logFile;
    }

    public static void close() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }
}
