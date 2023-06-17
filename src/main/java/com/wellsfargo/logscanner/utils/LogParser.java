package com.wellsfargo.logscanner.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

	private static LogEntry parseLogEntry(String logLine) {
		LogEntry logEntry = new LogEntry();

		// Assuming log line has a timestamp and message separated by a delimiter (e.g.,
		// "|")
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

	private static LocalDateTime getLogEntryTimestamp(LogEntry logEntry) {
		long timestamp = logEntry.getTimestamp();
		Instant instant = Instant.ofEpochMilli(timestamp);
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

}
