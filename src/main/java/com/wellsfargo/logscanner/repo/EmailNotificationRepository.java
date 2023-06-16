package com.wellsfargo.logscanner.repo;

import java.util.List;

import com.wellsfargo.logscanner.utils.EmailNotification;

public interface EmailNotificationRepository {
    void save(EmailNotification notification);
    List<EmailNotification> findAll();
}

