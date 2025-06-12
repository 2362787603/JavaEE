package com.example.demo.DAO;

import com.example.demo.Entity.Comment;
import java.util.List;

public interface CommentDao {
    // 创建评论，返回受影响行数或新评论ID
    int createNewComment(Comment comment);

    // 查某条帖子下的所有直接评论（commentID == 0）
    List<Comment> getAllCommentUnderPost(Integer postID);

    // 查某条评论下的所有回复
    List<Comment> getAllReplyUnderComment(Integer commentID);

    // 评论点赞：+1
    int likeComment(Integer commentID);
    int cancelLikeComment(Integer commentID);


    List<Comment> getAllCommentsByUser(String userID);

}
