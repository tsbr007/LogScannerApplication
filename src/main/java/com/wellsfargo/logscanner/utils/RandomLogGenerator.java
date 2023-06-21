package com.wellsfargo.logscanner.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomLogGenerator {
    private static final String[] LOG_LEVELS = { "DEBUG", "INFO", "WARN", "ERROR" };
    private static final String[] LOG_MESSAGES = {
        "Application started",
        "Invalid input detected",
        "Database connection failed",
        "User logged in",
        "File not found",
        "An error occurred"
    };

    private static final Random random = new Random();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy h:mm:ss a");

    public static void main(String[] args) {
        String logFolderPath = "C:/Balaji/Java/serverlogs";
        String logFilePath = logFolderPath + "/server.log";
        int numLogs = 10;

        try {
            createDirectory(logFolderPath);
            FileWriter fileWriter = new FileWriter(logFilePath, true);
            for (int i = 0; i < numLogs; i++) {
                String logLevel = getRandomLogLevel();
                String logMessage = getRandomLogMessage();
                String timestamp = generateTimestamp();
                String log = String.format("%s %s %s%n", timestamp, logLevel, logMessage);
                fileWriter.write(log);
            }
            fileWriter.close();
            System.out.println("Logs appended to file: " + logFilePath);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the log file: " + e.getMessage());
        }
    }

    private static void createDirectory(String logFolderPath) throws IOException {
        Path directoryPath = Paths.get(logFolderPath);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
    }

    private static String getRandomLogLevel() {
        int index = random.nextInt(LOG_LEVELS.length);
        return LOG_LEVELS[index];
    }

    private static String getRandomLogMessage() {
        int index = random.nextInt(LOG_MESSAGES.length);
        return LOG_MESSAGES[index];
    }

    private static String generateTimestamp() {
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}

