package com.wellsfargo.logscanner.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wellsfargo.logscanner.model.LogEntry;

public class LogParser {

    public static List<LogEntry> parseLogFile(File logFile) {
        List<String> logEntries = new ArrayList<>();
        List<LogEntry> logEntries1 = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logEntries.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String logEntry : logEntries) {
        	logEntries1.add(parseLogEntry(logEntry)); 
        }
        
        return logEntries1;
    }
    
    
    private static LogEntry parseLogEntry(String logLine) {
        LogEntry logEntry = new LogEntry();

        // Assuming  log line has a timestamp and message separated by a delimiter (e.g., "|")
        String[] parts = logLine.split("\\|");
        if (parts.length == 2) {
            try {
                // Parse the timestamp
                long timestamp = Long.parseLong(parts[0].trim());
                logEntry.setTimestamp(timestamp);
            } catch (NumberFormatException e) {
                // Handle parsing error if needed
            }

            // Set the log message
            logEntry.setMessage(parts[1].trim());
        } else {
            // Handle log line format error if needed
        }

        return logEntry;
    }

}
