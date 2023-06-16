package com.wellsfargo.logscanner.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wellsfargo.logscanner.model.LogEntry;
import com.wellsfargo.logscanner.service.LogScannerService;

import java.io.File;
import java.nio.file.*;
import java.util.List;

@Component
public class FileWatcher {

    private final LogScannerService logScannerService;
    
    @Value("${log.directory.path}") 
    private String logDirectoryPath;
    
    
    public FileWatcher(LogScannerService logScannerService) {
        this.logScannerService = logScannerService;
    }

    public void watch() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path directoryPath = Paths.get(logDirectoryPath);
            directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path logFile = pathEvent.context();

                    if (logFile.toString().endsWith(".log")) {
                        File file = directoryPath.resolve(logFile).toFile();
                        List<LogEntry> logEntries = LogParser.parseLogFile(file);
                        // Process log entries and send notifications
                        logScannerService.processLogEntries(logEntries);
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
