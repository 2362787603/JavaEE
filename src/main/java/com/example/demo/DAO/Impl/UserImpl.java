package com.example.demo.DAO.Impl;

import com.example.demo.DAO.UserDao;
import com.example.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> allUser() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public List<User> findById(String Id) {
        String sql = "SELECT * FROM users WHERE UserId = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), Id);
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (UserId, Username, Password, Level, Email, ImageId, Admin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, user.getUserId(), user.getUsername(), user.getPassword(), user.getLevel(), user.getEmail(), user.getImageId(), user.getAdmin());
        return rowsAffected > 0;
    }

    @Override
    public boolean updateById(User user) {
        String sql = "UPDATE users SET Username = ?, Password = ?, Level = ?, Email = ?, ImageId = ?, Admin = ? WHERE UserId = ?";
        int rowsAffected = jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getLevel(), user.getEmail(), user.getImageId(), user.getAdmin(), user.getUserId());
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteById(String Id) {
        String sql = "DELETE FROM users WHERE UserId = ?";
        int rowsAffected = jdbcTemplate.update(sql, Id);
        return rowsAffected > 0;
    }

    public User login(String userid, String password) {
        String sql = "SELECT * FROM users WHERE Userid = ? AND Password = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), userid, password);
        return users.isEmpty() ? null : users.get(0);
    }
}
