package com.wellsfargo.logscanner.model;

import java.time.LocalDateTime;

public class LogEntry {
    private LocalDateTime timestamp;
    private String logLevel;
    private String message;

    
    

    public LogEntry(LocalDateTime timestamp, String logLevel, String message) {
		super();
		this.timestamp = timestamp;
		this.logLevel = logLevel;
		this.message = message;
	}
    
    public LogEntry(LocalDateTime timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}



	public LocalDateTime getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}



	public String getLogLevel() {
		return logLevel;
	}



	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	@Override
    public String toString() {
        return "[" + timestamp + "] " + logLevel + ": " + message;
    }
}

