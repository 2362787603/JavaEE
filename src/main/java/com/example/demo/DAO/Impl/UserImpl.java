package com.example.demo.DAO.Impl;

import com.example.demo.DAO.UserDao;
import com.example.demo.Entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserImpl implements UserDao {

    private static final Logger userLogger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> allUser() {
        String sql = "SELECT * FROM users";
        userLogger.info("开始查询所有用户，SQL: {}", sql);
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        userLogger.info("查询所有用户完成，共查询到 {} 条记录", users.size());
        return users;
    }

    @Override
    public List<User> findById(String Id) {
        String sql = "SELECT * FROM users WHERE UserId = ?";
        userLogger.info("开始根据用户ID {} 查询用户，SQL: {}", Id, sql);
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), Id);
        userLogger.info("根据用户ID {} 查询用户完成，共查询到 {} 条记录", Id, users.size());
        return users;
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (UserId, Username, Password, Level, Email, Admin, avatarPath) VALUES (?, ?, ?, ?, ?, ?, ?)";
        userLogger.info("开始添加用户，用户ID: {}, SQL: {}", user.getUserId(), sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, user.getUserId(), user.getUsername(), user.getPassword(), user.getLevel(), user.getEmail(), user.getAdmin(), user.getAvatarPath());
            boolean result = rowsAffected > 0;
            userLogger.info("添加用户 {} 结果: {}", user.getUserId(), result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            userLogger.error("添加用户 {} 失败，错误信息: {}", user.getUserId(), e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateById(User user) {
        String sql = "UPDATE users SET Username = ?, Password = ?, Level = ?, Email = ?, Admin = ?, avatarPath = ? WHERE UserId = ?";
        userLogger.info("开始更新用户信息，用户ID: {}, SQL: {}", user.getUserId(), sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getLevel(), user.getEmail(), user.getAdmin(), user.getAvatarPath(), user.getUserId());
            boolean result = rowsAffected > 0;
            userLogger.info("更新用户 {} 信息结果: {}", user.getUserId(), result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            userLogger.error("更新用户 {} 信息失败，错误信息: {}", user.getUserId(), e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteById(String Id) {
        String sql = "DELETE FROM users WHERE UserId = ?";
        userLogger.info("开始删除用户，用户ID: {}, SQL: {}", Id, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, Id);
            boolean result = rowsAffected > 0;
            userLogger.info("删除用户 {} 结果: {}", Id, result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            userLogger.error("删除用户 {} 失败，错误信息: {}", Id, e.getMessage(), e);
            return false;
        }
    }

    public User login(String userid, String password) {
        String sql = "SELECT UserId, Username, Password, Level, Email, Admin, avatarPath FROM users WHERE UserId = ? AND Password = ?";
        userLogger.info("用户 {} 尝试登录，SQL: {}", userid, sql);
        try {
            List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), userid, password);
            User user = users.isEmpty() ? null : users.get(0);
            userLogger.info("用户 {} 登录结果: {}", userid, user != null ? "成功" : "失败");
            return user;
        } catch (Exception e) {
            userLogger.error("用户 {} 登录时发生错误，错误信息: {}", userid, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User findUserById(String id) {
        userLogger.info("开始根据用户ID {} 查找单个用户", id);
        List<User> users = findById(id);
        User user = users.isEmpty() ? null : users.get(0);
        userLogger.info("根据用户ID {} 查找单个用户结果: {}", id, user != null ? "找到" : "未找到");
        return user;
    }

    @Override
    public boolean updateAvatarPath(String userId, String avatarPath) {
        String sql = "UPDATE users SET avatarPath = ? WHERE UserId = ?";
        userLogger.info("开始更新用户 {} 的头像路径，SQL: {}", userId, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, avatarPath, userId);
            boolean result = rowsAffected > 0;
            userLogger.info("更新用户 {} 的头像路径结果: {}", userId, result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            userLogger.error("更新用户 {} 的头像路径失败，错误信息: {}", userId, e.getMessage(), e);
            return false;
        }
    }
}
