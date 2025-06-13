package com.example.demo.DAO.Impl;

import com.example.demo.DAO.ForumDao;
import com.example.demo.Entity.Forum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


@Repository
public class ForumImpl implements ForumDao {

    private static final Logger forumLogger = LoggerFactory.getLogger(ForumImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Forum> forumMapper = new RowMapper<>() {
        @Override
        public Forum mapRow(ResultSet rs, int rowNum) throws SQLException {
            Forum forum = new Forum();
            forum.setId(rs.getInt("id"));
            forum.setUserID(rs.getString("userId"));
            forum.setName(rs.getString("name"));
            forum.setIntroduction(rs.getString("introduction"));
            forum.setFollowCount(rs.getInt("follow_count"));
            forum.setPostCount(rs.getInt("post_count")); 
            forum.setImageId(rs.getInt("imageId")); // 新增图片 ID 映射
            return forum;
        }
    };

    @Override
    public Integer createForum(String userID, String name, String introduction, Integer imageId) {
        String sql = "INSERT INTO forums (userID, name, introduction, imageId) VALUES (?, ?, ?, ?)";
        forumLogger.info("开始创建论坛，用户ID: {}, 论坛名称: {}, 图片ID: {}, SQL: {}", userID, name, imageId, sql);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                java.sql.PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, userID);
                ps.setString(2, name);
                ps.setString(3, introduction);
                ps.setInt(4, imageId); // 设置图片 ID
                return ps;
            }, keyHolder);
            Integer forumId = keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
            forumLogger.info("成功创建论坛，用户ID: {}, 论坛ID: {}, 图片ID: {}", userID, forumId, imageId);
            return forumId;
        } catch (Exception e) {
            forumLogger.error("创建论坛失败，用户ID: {}, 论坛名称: {}, 图片ID: {}, 错误信息: {}", userID, name, imageId, e.getMessage(), e);
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

    @Override
    public List<Forum> getAllForum() {
        String sql = "SELECT f.*, COUNT(p.id) as post_count " +
                     "FROM forums f " +
                     "LEFT JOIN post p ON f.id = p.forum_id " +
                     "GROUP BY f.id";
        forumLogger.info("开始执行获取所有论坛信息的查询，SQL: {}", sql);
        try {
            List<Forum> result = jdbcTemplate.query(sql, forumMapper);
            forumLogger.info("成功获取所有论坛信息，共查询到 {} 条记录", result.size());
            return result;
        } catch (Exception e) {
            forumLogger.error("获取所有论坛信息失败，错误信息: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据论坛名称模糊查询论坛信息，包含每个论坛的帖子数量
     * @param name 用于模糊查询的论坛名称
     * @return 包含匹配论坛信息的列表
     */
    @Override
    public List<Map<String, Object>> getAllForumByNameWithPostCount(String name) {
        String sql = "SELECT f.*, COUNT(p.id) as post_count " +
                     "FROM forums f " +
                     "LEFT JOIN post p ON f.id = p.forum_id " +
                     "WHERE f.name LIKE ? " +
                     "GROUP BY f.id";
        String searchName = "%" + name + "%";
        forumLogger.info("开始执行根据名称 {} 模糊查询论坛信息的查询，SQL: {}", name, sql);
        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, searchName);
            forumLogger.info("成功根据名称 {} 模糊查询论坛信息，共查询到 {} 条记录", name, result.size());
            return result;
        } catch (Exception e) {
            forumLogger.error("根据名称 {} 模糊查询论坛信息失败，错误信息: {}", name, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据用户 ID 搜索论坛
     * @param userId 用户 ID
     * @return 包含匹配论坛信息的列表
     */
    @Override
    public List<Forum> getForumByUserId(String userId) {
        String sql = "SELECT f.*,COUNT(p.id) as post_count FROM forums f LEFT JOIN post p ON f.id = p.forum_id  WHERE f.UserId = ? GROUP BY f.id";
        forumLogger.info("开始执行根据用户 ID {} 搜索论坛信息的查询，SQL: {}", userId, sql);
        try {
            List<Forum> result = jdbcTemplate.query(sql, forumMapper, userId);
            forumLogger.info("成功根据用户 ID {} 搜索论坛信息，共查询到 {} 条记录", userId, result.size());
            return result;
        } catch (Exception e) {
            forumLogger.error("根据用户 ID {} 搜索论坛信息失败，错误信息: {}", userId, e.getMessage(), e);
            return null;
        }
    }
}