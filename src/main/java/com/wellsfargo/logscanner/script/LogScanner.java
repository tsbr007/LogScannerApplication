package com.wellsfargo.logscanner.script;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LogScanner {
    private static final String LOG_FILE_PATH = "/path/to/server.log";
    private static final long SCAN_INTERVAL_MS = 5000; // 5 seconds
    private static final long MAX_SCAN_DURATION_MS = 60000; // 1 minute
    private static final String SEARCH_KEYWORD = "fail"; // Message to search for in the log file

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + MAX_SCAN_DURATION_MS;

        while (System.currentTimeMillis() < endTime) {
            try {
                File logFile = new File(LOG_FILE_PATH);

                if (logFile.exists()) {
                    // Get the size of the log file
                    long fileSize = logFile.length();

                    // Check if the log file has been modified
                    if (fileSize > 0) {
                        long lastModifiedTime = logFile.lastModified();

                        if (lastModifiedTime > startTime) {
                            long readFromByte = Math.max(0, fileSize - 1500); // Read the last 1500 bytes from the log file
                            String logContent = readLogContent(logFile, readFromByte);

                            if (logContent.contains(SEARCH_KEYWORD)) {
                                // Send email to recipients
                                sendEmail("Log file contains the keyword: " + SEARCH_KEYWORD);
                            }

                            // Update the start time for the next scan
                            startTime = System.currentTimeMillis();
                        }
                    }
                }

                // Wait for the specified interval before the next scan
                Thread.sleep(SCAN_INTERVAL_MS);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String readLogContent(File logFile, long readFromByte) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(logFile, "r")) {
            randomAccessFile.seek(readFromByte);

            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = randomAccessFile.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append(System.lineSeparator());
            }

            return contentBuilder.toString();
        }
    }

    private static void sendEmail(String message) {
        // Implement email sending logic here
        System.out.println("Sending email: " + message);
    }
}

