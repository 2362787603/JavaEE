package com.example.demo.DAO.Impl;

import com.example.demo.DAO.ForumDao;
import com.example.demo.Entity.Forum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
// Add the missing import statement
import java.sql.Statement;

@Repository
public class ForumImpl implements ForumDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer createForum(String userID, String name, String introduction) {
        String sql = "INSERT INTO forums (userID, name, introduction) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            java.sql.PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userID);
            ps.setString(2, name);
            ps.setString(3, introduction);
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
    }

    @Override
    public Forum searchForum(Integer id) {
        String sql = "SELECT * FROM forums WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Forum.class), id);
    }

    @Override
    public boolean deleteForum(Integer id) {
        String sql = "DELETE FROM forums WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
    @Override
    public boolean updateForum(Integer id, String name, String introduction) {
        String sql = "UPDATE forums SET name = ?, introduction = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, name, introduction, id);
        return rowsAffected > 0;
    }

    @Override
    public boolean updateForumName(Integer id, String name) {
        String sql = "UPDATE forums SET name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, name, id);
        return rowsAffected > 0;
    }

    @Override
    public boolean updateForumIntroduction(Integer id, String introduction) {
        String sql = "UPDATE forums SET introduction = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, introduction, id);
        return rowsAffected > 0;
    }
}