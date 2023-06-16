package com.wellsfargo.logscanner.service;


import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wellsfargo.logscanner.model.LogEntry;
import com.wellsfargo.logscanner.repo.EmailNotificationRepository;
import com.wellsfargo.logscanner.utils.EmailNotification;
import com.wellsfargo.logscanner.utils.LogParser;

@Service
public class LogScannerService {

    private final EmailNotificationRepository notificationRepository;
    private final EmailService emailService;

    @Autowired
    public LogScannerService(EmailNotificationRepository notificationRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    public List<EmailNotification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public void scanLogsAndSendNotifications() {
        // Specify the log directory path
        String logDirectoryPath = "path/to/log/directory";

        // Iterate through log files in the directory
        File logDirectory = new File(logDirectoryPath);
        File[] logFiles = logDirectory.listFiles();

        if (logFiles != null) {
            for (File logFile : logFiles) {
                // Check if the log file is modified or new
                if (logFile.lastModified() > System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
                    // Parse the log file and extract relevant information
                    List<LogEntry> logEntries = LogParser.parseLogFile(logFile);

                    // Process the log entries and generate email content
                    String emailContent = processLogEntries(logEntries);

                    // Send email notification
                    EmailNotification notification = new EmailNotification();
                    notification.setSubject("Log Notification");
                    notification.setContent(emailContent);
                    saveNotificationAndSendEmail(notification);
                }
            }
        }
    }

    public void saveNotificationAndSendEmail(EmailNotification notification) {
        notificationRepository.save(notification);
        emailService.sendEmailNotification(notification);
    }

    public String processLogEntries(List<LogEntry> logEntries) {
        // Process the log entries to generate the email content
        StringBuilder sb = new StringBuilder();
        for (LogEntry logEntry : logEntries) {
            sb.append(logEntry.getMessage()).append("\n");
        }
        return sb.toString();
    }
}
