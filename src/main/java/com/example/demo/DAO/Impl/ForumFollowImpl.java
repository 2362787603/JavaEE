package com.example.demo.DAO.Impl;

import com.example.demo.DAO.ForumFollowDao;
import com.example.demo.Entity.ForumFollow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class ForumFollowImpl implements ForumFollowDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer addFollow(ForumFollow follow) {
        String sql = "INSERT INTO forum_follow (user_id, forum_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, follow.getUserId());
            ps.setInt(2, follow.getForumId());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : null;
    }

    @Override
    public boolean removeFollow(ForumFollow follow) {
        String sql = "DELETE FROM forum_follow WHERE user_id = ? AND forum_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, follow.getUserId(), follow.getForumId());
        return rowsAffected > 0;
    }
}