package com.example.demo.DAO.Impl;

import com.example.demo.DAO.ForumFollowDao;
import com.example.demo.Entity.Forum;
import com.example.demo.Entity.ForumFollow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ForumFollowImpl implements ForumFollowDao {

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
            return forum;
        }
    };

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

    @Override
    public List<Forum> getAllUserFollow(String userId) {
        String sql = "SELECT f.* FROM forums f " +
                     "JOIN forum_follow ff ON f.id = ff.forum_id " +
                     "WHERE ff.user_id = ?";
        return jdbcTemplate.query(sql, forumMapper, userId);
    }
}