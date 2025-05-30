package com.example.demo.DAO.Impl;

import com.example.demo.DAO.PostDao;
import com.example.demo.Entity.Post;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PostDaoImpl implements PostDao {

    private final JdbcTemplate jdbc;

    public PostDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Post> postMapper = new RowMapper<>() {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post p = new Post();
            p.setId(rs.getInt("id"));
            p.setUserID(rs.getInt("user_id"));
            p.setForumID(rs.getInt("forum_id"));
            p.setTitle(rs.getString("title"));
            p.setContent(rs.getString("content"));
            p.setLikeNumber(rs.getInt("like_number"));
            p.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            return p;
        }
    };

    @Override
    public int createNewPost(Post post) {
        String sql = """
            INSERT INTO post(user_id, forum_id, title, content, like_number, create_time)
            VALUES (?, ?, ?, ?, 0, NOW())
        """;
        return jdbc.update(sql,
                post.getUserID(),
                post.getForumID(),
                post.getTitle(),
                post.getContent()
        );
    }

    @Override
    public Post getPost(Integer id) {
        String sql = "SELECT * FROM post WHERE id = ?";
        return jdbc.queryForObject(sql, postMapper, id);
    }

    @Override
    public List<Post> getAllPostInForum(Integer forumID) {
        String sql = "SELECT * FROM post WHERE forum_id = ? ORDER BY create_time DESC";
        return jdbc.query(sql, postMapper, forumID);
    }

    @Override
    public List<Post> getAllUserPost(Integer userID) {
        String sql = "SELECT * FROM post WHERE user_id = ? ORDER BY create_time DESC";
        return jdbc.query(sql, postMapper, userID);
    }

    @Override
    public int likePost(Integer postID) {
        String sql = "UPDATE post SET like_number = like_number + 1 WHERE id = ?";
        return jdbc.update(sql, postID);
    }


    @Override
    public String testConnection() {
        String sql = "SELECT 1";
        Integer result = jdbc.queryForObject(sql, Integer.class);
        return result != null && result == 1 ? "OK" : "FAIL";
    }

}
