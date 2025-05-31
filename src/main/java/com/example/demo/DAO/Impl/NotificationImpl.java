package com.example.demo.DAO.Impl;

import com.example.demo.DAO.NotificationDao;
import com.example.demo.Entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationImpl implements NotificationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createNewInform(String userID, String content) {
        String sql = "INSERT INTO notifications (user_id, content, Read, create_time) VALUES (?, ?, false, NOW())";
        jdbcTemplate.update(sql, userID, content);
    }

    @Override
    public List<Notification> getAllUserInform(String userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY create_time DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notification.class), userId);
    }

    @Override
    public void updateIsRead(Integer id, boolean Read) {
        String sql = "UPDATE notifications SET Read = ? WHERE id = ?";
        jdbcTemplate.update(sql, Read, id);
    }
}