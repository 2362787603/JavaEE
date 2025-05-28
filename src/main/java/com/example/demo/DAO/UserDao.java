package com.example.demo.DAO;
import com.example.demo.Entity.*;
import java.util.List;

public interface UserDao {
    List<User> allUser();
    List<User> findById(String Id);
    boolean addUser(User user);
    boolean updateById(User user);
    boolean deleteById(String Id);
}
