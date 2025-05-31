package com.example.demo.DAO;

import com.example.demo.Entity.Notification;
import java.util.List;

public interface NotificationDao {
    /**
     * 产生新的通知
     * @param userID 用户 ID
     * @param content 通知内容
     */
    void createNewInform(String userID, String content);

    /**
     * 获取某个用户的所有通知
     * @param userId 用户 ID
     * @return 用户的所有消息列表
     */
    List<Notification> getAllUserInform(String userId);

    /**
     * 修改通知的阅读状态
     * @param id 通知 ID
     * @param isRead 阅读状态
     */
    void updateIsRead(Integer id, boolean isRead);
}