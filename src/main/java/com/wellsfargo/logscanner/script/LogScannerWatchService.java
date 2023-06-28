package com.wellsfargo.logscanner.script;

import java.io.IOException;
import java.nio.file.*;

public class LogScannerWatchService {
	private static final String LOG_FILE_PATH = "C:/Balaji/Java/serverlogs/server.log";
    private static final String[] KEYWORDS_TO_SEARCH = {"error", "fail"};
    private static final String RECIPIENT_EMAIL = "recipient@example.com";

    public static void main(String[] args) throws IOException {
        // Start watching the log file for modifications
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path logFilePath = Paths.get(LOG_FILE_PATH);
        Path logFileDirectory = logFilePath.getParent();
        logFileDirectory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);

        System.out.println("LogScanner started. Watching file: " + LOG_FILE_PATH);

        while (true) {
            try {
                // Wait for a file modification event
                WatchKey key = watchService.take();

                // Process all events in the key
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.context().equals(logFilePath.getFileName())) {
                        System.out.println("Log file modified: " + LOG_FILE_PATH);

                        // Read and scan the log file
                        String logContent = readLogFile(logFilePath);
                        boolean containsKeywords = checkKeywords(logContent);
                        
                        // Send email if keywords are found
                        if (containsKeywords) {
                            sendEmail(logContent);
                            System.out.println("Email sent to: " + RECIPIENT_EMAIL);
                        }
                    }
                }

                // Reset the key to receive further file modification events
                key.reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String readLogFile(Path logFilePath) throws IOException {
        byte[] fileBytes = Files.readAllBytes(logFilePath);
        return new String(fileBytes);
    }

    private static boolean checkKeywords(String logContent) {
        for (String keyword : KEYWORDS_TO_SEARCH) {
            if (logContent.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private static void sendEmail(String logContent) {
        System.out.println("Email Sent....");
    }
}

