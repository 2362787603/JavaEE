package com.example.demo.DAO.Impl;

import com.example.demo.DAO.LikeDao;
import com.example.demo.DAO.UserDao;
import com.example.demo.Entity.Like;
import com.example.demo.Entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeImpl implements LikeDao {

    private static final Logger userLogger = LoggerFactory.getLogger(LikeImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Like> getAllLikes(String userID) {
        String sql = "SELECT * FROM userlike WHERE user_id = ?";
        userLogger.info("开始查询用户ID {} 的所有点赞，SQL: {}", userID, sql);
        List<Like> likeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Like.class), userID);
        userLogger.info("根据用户ID {} 查询用户点赞完成，共查询到 {} 条记录", userID, likeList.size());
        return likeList;
    }


    @Override
    public int createNewLike(Like like) {
        String sql = "INSERT INTO userlike(id, user_id, post_id, comment_id) VALUES (?, ?, ?, ?)";
        String sql2 = "SELECT count(*) from userlike where user_id=? and post_id=? and comment_id=?";
        userLogger.info("开始添加用户点赞，用户ID: {}, SQL: {}",like.getUserId(), sql);
        try{
            Integer rows = jdbcTemplate.queryForObject(sql2, Integer.class, like.getUserId(), like.getPostId(), like.getCommentId());
            if(rows != null && rows == 0){
                int rowsAffected = jdbcTemplate.update(sql,like.getId(),like.getUserId(),like.getPostId(),like.getCommentId());
                int result = rowsAffected > 0 ? 0 : -1;
                userLogger.info("添加用户 {} 结果: {}", like.getUserId(), result == 0? "成功" : "失败");
            }
            return 0;
        }catch (Exception e){
            userLogger.error("添加用户 {} 失败，错误信息: {}", like.getUserId(), e.getMessage(), e);
            return -1;
        }
    }


    @Override
    public boolean deleteUserLike(Like like) {
        String sql = "DELETE FROM userlike WHERE user_id = ? and post_id = ? and comment_id = ?";
        userLogger.info("开始取消用户点赞，用户ID: {}, SQL: {}", like.getUserId(), sql);
        try {
            int rowsAffected = jdbcTemplate.update(sql,like.getUserId(),like.getPostId(),like.getCommentId());
            return rowsAffected > 0;
        } catch (Exception e) {
            userLogger.error("删除用户 {} 失败，错误信息: {}", like.getUserId(), e.getMessage(), e);
            return false;
        }
    }


    @Override
    public boolean getUserLikePost(String userID, Integer postID) {
        String sql = "SELECT * FROM userlike WHERE user_id = ? and post_id = ?";
        userLogger.info("用户 {} 尝试获取帖子点赞情况，SQL: {}", userID, sql);
        try {
            List<Like> likeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Like.class), userID, postID);
            return !likeList.isEmpty();
        } catch (Exception e) {
            userLogger.error("用户 {} 登录时发生错误，错误信息: {}", userID, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean getUserLikeComment(String userID, Integer commentID) {
        String sql = "SELECT * FROM userlike WHERE user_id = ? and comment_id = ?";
        userLogger.info("用户 {} 尝试获取评论点赞情况，SQL: {}", userID, sql);
        try {
            List<Like> likeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Like.class), userID, commentID);
            return !likeList.isEmpty();
        } catch (Exception e) {
            userLogger.error("用户 {} 登录时发生错误，错误信息: {}", userID, e.getMessage(), e);
            return false;
        }
    }

}
