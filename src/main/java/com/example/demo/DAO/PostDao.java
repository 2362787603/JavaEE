package com.example.demo.DAO;

import com.example.demo.Entity.Post;
import java.util.List;

public interface PostDao {
    // 创建新帖，返回受影响行数或新帖ID
    int createNewPost(Post post);

    // 根据帖子ID查一条
    Post getPost(Integer id);

    // 查某个论坛下所有帖子
    List<Post> getAllPostInForum(Integer forumID);

    // 查某个用户的所有帖子
    List<Post> getAllUserPost(Integer userID);

    // 帖子点赞：+1，返回更新行数
    int likePost(Integer postID);

    String testConnection();

    // PostDao.java
    List<Post> getAllPosts();

    List<Post> searchPostsByTitle(String keyword);


    int getCommentCountByPostId(int postId);

}
