package com.example.demo.DAO;

import com.example.demo.Entity.ForumFollow;

public interface ForumFollowDao {
    Integer addFollow(ForumFollow follow);
    boolean removeFollow(ForumFollow follow);
}