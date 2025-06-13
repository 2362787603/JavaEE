package com.example.demo.DAO;

import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Like;

import java.util.List;

public interface LikeDao {
    // 创建评论，返回受影响行数或新评论ID
    int createNewLike(Like like);

    //判断用户是否点赞了某帖子
    boolean getUserLikePost(String userID,Integer postID);

    //判断用户是否点赞了某评论
    boolean getUserLikeComment(String userID,Integer commentID);

    //用户取消点赞
    boolean deleteUserLike(Like like);

    // 查看用户所有的点赞记录
    List<Like> getAllLikes(String userID);
}
