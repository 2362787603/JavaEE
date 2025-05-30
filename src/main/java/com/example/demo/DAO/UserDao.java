package com.example.demo.DAO;
import com.example.demo.Entity.*;
import java.util.List;

public interface UserDao {
    List<User> allUser();
    List<User> findById(String Id);
    boolean addUser(User user);
    boolean updateById(User user);
    boolean deleteById(String Id);
    User findUserById(String id);
    // 添加更新头像路径的方法
    boolean updateAvatarPath(String userId, String avatarPath);
}
