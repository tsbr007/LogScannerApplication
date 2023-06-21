package com.wellsfargo.logscanner.script;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class LogScanner {
	private static final String LOG_FILE_PATH = "C:/Balaji/Java/serverlogs/server.log";
	private static final long SCAN_INTERVAL_MS = 5000; // 5 seconds
	private static final long MAX_SCAN_DURATION_MS = 300000; // 5 minute
	private static final long BYTE_TO_READ = 1024; // BYTES TO READ

	private static List<String> SEARCH_KEYWORDS = Arrays.asList("fail", "successfully", "error", "hung"); // Message to
																											// search
																											// for in
																											// the log
																											// file
	private static String SUCCESSFULLY = "successfully";
	private static String ERROR = "error";

	private static String HUNG = "hung";

	public static void main(String[] args) {

		long currentTimeMillis = System.currentTimeMillis();

		// Convert current time in milliseconds to LocalDateTime
		LocalDateTime currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis),
				ZoneId.systemDefault());

		// Subtract 1 minute
		LocalDateTime modifiedDateTime = currentDateTime.minusMinutes(1);

		// Convert modified LocalDateTime back to milliseconds
		long startTime = modifiedDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

		long endTime = startTime + MAX_SCAN_DURATION_MS;

		while (System.currentTimeMillis() < endTime) {
			System.out.println("loop starts.. "+System.currentTimeMillis());
			try {
				File logFile = new File(LOG_FILE_PATH);

				if (logFile.exists()) {
					// Get the size of the log file
					long fileSize = logFile.length();

					// Check if the log file has been modified
					if (fileSize > 0) {
						long lastModifiedTime = logFile.lastModified();

						if (lastModifiedTime > startTime) {
							long readFromByte = Math.max(0, fileSize - BYTE_TO_READ); // Read the last 1500 bytes from
																						// the log file
							String logContent = readLogContent(logFile, readFromByte);

							if (logContent.contains(ERROR)) {
								sendEmail("Started with errors/warnings/exceptions");
							} else if (logContent.contains(SUCCESSFULLY)) {
								sendEmail("Started Successfully");
							} else if (logContent.contains(HUNG)) {
								sendEmail("Server went into hung status and not started properly");
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
		// email sending logic
		System.out.println("Sending email at :"+System.currentTimeMillis()+ ":: "+ message);
	}
}
