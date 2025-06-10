package com.example.demo.DAO;

import com.example.demo.Entity.ForumFollow;

import java.util.List;
import com.example.demo.Entity.Forum;
import com.example.demo.Entity.Post;

public interface ForumFollowDao {
    Integer addFollow(ForumFollow follow);
    boolean removeFollow(ForumFollow follow);

    List<Forum> getAllUserFollow(String userId);

    /**
     * 获取指定用户关注的所有论坛的所有帖子
     * @param userId 用户 ID
     * @return 用户关注的所有论坛的所有帖子列表
     */
    List<Post> getAllPostsOfUserFollowedForums(String userId);
}