package com.wellsfargo.logscanner.repo.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.wellsfargo.logscanner.repo.EmailNotificationRepository;
import com.wellsfargo.logscanner.utils.EmailNotification;

@Repository
public class EmailNotificationRepositoryImpl implements EmailNotificationRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public EmailNotificationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(EmailNotification notification) {
        String sql = "INSERT INTO email_notifications (subject, content) VALUES (?, ?)";
        jdbcTemplate.update(sql, notification.getSubject(), notification.getContent());
    }

    @Override
    public List<EmailNotification> findAll() {
        String sql = "SELECT subject, content FROM email_notifications";
        return jdbcTemplate.query(sql, new EmailNotificationMapper());
    }

    private static class EmailNotificationMapper implements RowMapper<EmailNotification> {
        @Override
        public EmailNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmailNotification notification = new EmailNotification();
            notification.setSubject(rs.getString("subject"));
            notification.setContent(rs.getString("content"));
            return notification;
        }
    }
}
