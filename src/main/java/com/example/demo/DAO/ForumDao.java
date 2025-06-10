package com.example.demo.DAO;

import java.util.List;
import java.util.Map;
import com.example.demo.Entity.Forum;

public interface ForumDao {
    Integer createForum(String userID, String name, String introduction);
    Forum searchForum(Integer id);
    // 添加删除方法
    boolean deleteForum(Integer id);
    // 添加修改方法
    boolean updateForum(Integer id,  String name, String introduction);
    boolean updateForumName(Integer id, String name);
    boolean updateForumIntroduction(Integer id, String introduction);
    boolean increaseFollowCount(Integer id); // 新增增加计数方法
    boolean decreaseFollowCount(Integer id);
    Integer getFollowCount(Integer id); // 新增查看计数方法


    List<Forum> getAllForum();



    List<Map<String, Object>> getAllForumByNameWithPostCount(String name);
}