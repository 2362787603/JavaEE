package com.example.demo.DAO;

import com.example.demo.Entity.ForumFollow;

import java.util.List;
import com.example.demo.Entity.Forum;

public interface ForumFollowDao {
    Integer addFollow(ForumFollow follow);
    boolean removeFollow(ForumFollow follow);

    List<Forum> getAllUserFollow(String userId);
}