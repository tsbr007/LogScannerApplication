package com.wellsfargo.logscanner.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wellsfargo.logscanner.model.LogEntry;

public class LogParser {

	public static List<LogEntry> parseLogFile(File logFile) {
		List<LogEntry> logEntries = new ArrayList<>();
		LocalDateTime currentTimeMinusOneMinute = LocalDateTime.now().minusMinutes(1);

		try (RandomAccessFile randomAccessFile = new RandomAccessFile(logFile, "r")) {
			long fileLength = randomAccessFile.length();
			long filePointer = fileLength - 1;

			while (filePointer >= 0) {
				randomAccessFile.seek(filePointer);
				int readByte = randomAccessFile.readByte();

				if (readByte == 0xA) {
					String line = randomAccessFile.readLine();
					if (line != null && !line.isEmpty()) {
						LogEntry logEntry = parseLogEntry(line);
						LocalDateTime logEntryTimestamp = getLogEntryTimestamp(logEntry);
						if (logEntryTimestamp != null && logEntryTimestamp.isAfter(currentTimeMinusOneMinute)) {
							logEntries.add(logEntry);
							Collections.reverse(logEntries); // Reverse the list to maintain ascending order of
																// timestamps
							return logEntries;
						}
						else {
							break;
						}
						
					}
				}
				filePointer--;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.reverse(logEntries); // Reverse the list to maintain ascending order of timestamps
		return logEntries;
	}

	private static LogEntry parseLogEntry(String line) {
	    try {
	    	//Sample : [2023-06-17T09:45:12Z] INFO: Initializing ProtocolHandler
	        // Parse the timestamp, log level, and message from the log line
	        String timestampStr = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
	        LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_DATE_TIME);

	        String logLevel = line.substring(line.indexOf(" ") + 1, line.indexOf(":"));
	        String message = line.substring(line.indexOf(":") + 2); // Adding 2 to skip the ": " characters

	        // Create and return the LogEntry object
	        return new LogEntry(timestamp, logLevel, message);
	    } catch (Exception e) {
	        // Error occurred while parsing the log line
	        // You can handle the exception or log it if needed
	        e.printStackTrace();
	        return null; // Return null if unable to parse the log line
	    }
	}

	private static LocalDateTime getLogEntryTimestamp(LogEntry logEntry) {
		LocalDateTime timestamp = logEntry.getTimestamp(); 
		Instant instant = timestamp.toInstant(ZoneOffset.UTC);
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

}
