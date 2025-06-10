package com.example.demo.DAO.Impl;

import com.example.demo.DAO.ForumDao;
import com.example.demo.Entity.Forum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;

@Repository
public class ForumImpl implements ForumDao {

    private static final Logger forumLogger = LoggerFactory.getLogger(ForumImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer createForum(String userID, String name, String introduction) {
        String sql = "INSERT INTO forums (userID, name, introduction) VALUES (?, ?, ?)";
        forumLogger.info("开始创建论坛，用户ID: {}, 论坛名称: {}, SQL: {}", userID, name, sql);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                java.sql.PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, userID);
                ps.setString(2, name);
                ps.setString(3, introduction);
                return ps;
            }, keyHolder);
            Integer forumId = keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
            forumLogger.info("成功创建论坛，用户ID: {}, 论坛ID: {}", userID, forumId);
            return forumId;
        } catch (Exception e) {
            forumLogger.error("创建论坛失败，用户ID: {}, 论坛名称: {}, 错误信息: {}", userID, name, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Forum searchForum(Integer id) {
        String sql = "SELECT * FROM forums WHERE id = ?";
        forumLogger.info("开始查询论坛，论坛ID: {}, SQL: {}", id, sql);
        try {
            Forum forum = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Forum.class), id);
            if (forum != null) {
                forumLogger.info("成功查询论坛，论坛ID: {}", id);
            } else {
                forumLogger.warn("未找到论坛，论坛ID: {}", id);
            }
            return forum;
        } catch (Exception e) {
            forumLogger.error("查询论坛失败，论坛ID: {}, 错误信息: {}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Forum> getAllForum(){
        String sql = "SELECT * FROM forums";
        forumLogger.info("开始查询论坛，SQL: {}",  sql);
        try {
            List<Forum> forumList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Forum.class));
            forumLogger.info("成功查询论坛，论坛ID");
            return forumList;
        } catch (Exception e) {
            forumLogger.error("查询论坛失败 错误信息: {}",e.getMessage(), e);
            return null;
        }
    }


    @Override
    public boolean deleteForum(Integer id) {
        String sql = "DELETE FROM forums WHERE id = ?";
        forumLogger.info("开始删除论坛，论坛ID: {}, SQL: {}", id, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, id);
            boolean result = rowsAffected > 0;
            forumLogger.info("删除论坛 {} 结果: {}", id, result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            forumLogger.error("删除论坛 {} 失败，错误信息: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateForum(Integer id, String name, String introduction) {
        String sql = "UPDATE forums SET name = ?, introduction = ? WHERE id = ?";
        forumLogger.info("开始更新论坛信息，论坛ID: {}, 新名称: {}, 新简介: {}, SQL: {}", id, name, introduction, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, name, introduction, id);
            boolean result = rowsAffected > 0;
            forumLogger.info("更新论坛 {} 信息结果: {}", id, result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            forumLogger.error("更新论坛 {} 信息失败，错误信息: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateForumName(Integer id, String name) {
        String sql = "UPDATE forums SET name = ? WHERE id = ?";
        forumLogger.info("开始更新论坛名称，论坛ID: {}, 新名称: {}, SQL: {}", id, name, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, name, id);
            boolean result = rowsAffected > 0;
            forumLogger.info("更新论坛 {} 名称结果: {}", id, result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            forumLogger.error("更新论坛 {} 名称失败，错误信息: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateForumIntroduction(Integer id, String introduction) {
        String sql = "UPDATE forums SET introduction = ? WHERE id = ?";
        forumLogger.info("开始更新论坛简介，论坛ID: {}, 新简介: {}, SQL: {}", id, introduction, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, introduction, id);
            boolean result = rowsAffected > 0;
            forumLogger.info("更新论坛 {} 简介结果: {}", id, result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            forumLogger.error("更新论坛 {} 简介失败，错误信息: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean increaseFollowCount(Integer id) {
        String sql = "UPDATE forums SET follow_count = follow_count + 1 WHERE id = ?";
        forumLogger.info("开始增加论坛 {} 的关注数，SQL: {}", id, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, id);
            boolean result = rowsAffected > 0;
            forumLogger.info("增加论坛 {} 关注数结果: {}", id, result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            forumLogger.error("增加论坛 {} 关注数失败，错误信息: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean decreaseFollowCount(Integer id) {
        String sql = "UPDATE forums SET follow_count = follow_count - 1 WHERE id = ? AND follow_count > 0";
        forumLogger.info("开始减少论坛 {} 的关注数，SQL: {}", id, sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql, id);
            boolean result = rowsAffected > 0;
            forumLogger.info("减少论坛 {} 关注数结果: {}", id, result ? "成功" : "失败");
            return result;
        } catch (Exception e) {
            forumLogger.error("减少论坛 {} 关注数失败，错误信息: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Integer getFollowCount(Integer id) {
        String sql = "SELECT follow_count FROM forums WHERE id = ?";
        forumLogger.info("开始查询论坛 {} 的关注数，SQL: {}", id, sql);
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
            forumLogger.info("查询论坛 {} 的关注数结果: {}", id, count);
            return count;
        } catch (Exception e) {
            forumLogger.error("查询论坛 {} 关注数失败，错误信息: {}", id, e.getMessage(), e);
            return null;
        }
    }
}