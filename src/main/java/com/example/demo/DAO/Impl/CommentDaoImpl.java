package com.example.demo.DAO.Impl;

import com.example.demo.DAO.CommentDao;
import com.example.demo.Entity.Comment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CommentDaoImpl implements CommentDao {

    private final JdbcTemplate jdbc;

    public CommentDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Comment> commentMapper = new RowMapper<>() {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Comment c = new Comment();
            c.setId(rs.getInt("id"));
            c.setUserID(rs.getString("user_id"));
            c.setPostID(rs.getInt("post_id"));
            c.setCommentID(rs.getInt("comment_id"));
            c.setCommentContent(rs.getString("comment_content"));
            c.setLikeNumber(rs.getInt("like_number"));
            c.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            return c;
        }
    };

    @Override
    public int createNewComment(Comment comment) {
        String sql = """
            INSERT INTO comment(user_id, post_id, comment_id, comment_content, like_number,create_time)
            VALUES (?, ?, ?, ?, 0,NOW())
        """;
        return jdbc.update(sql,
                comment.getUserID(),
                comment.getPostID(),
                comment.getCommentID(),
                comment.getCommentContent()
        );
    }

    @Override
    public List<Comment> getAllCommentUnderPost(Integer postID) {
        String sql = "SELECT * FROM comment WHERE post_id = ? AND comment_id = 0 ORDER BY id";
        return jdbc.query(sql, commentMapper, postID);
    }

    @Override
    public List<Comment> getAllReplyUnderComment(Integer commentID) {
        String sql = "SELECT * FROM comment WHERE comment_id = ? ORDER BY id";
        return jdbc.query(sql, commentMapper, commentID);
    }

    @Override
    public int likeComment(Integer commentID) {
        String sql = "UPDATE comment SET like_number = like_number + 1 WHERE id = ?";
        return jdbc.update(sql, commentID);
    }

    @Override
    public int cancelLikeComment(Integer commentID){
        String sql = "UPDATE comment SET like_number = like_number - 1 WHERE id = ?";
        return jdbc.update(sql, commentID);
    }
}
