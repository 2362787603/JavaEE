package com.example.demo.Controller;

import com.example.demo.DAO.Impl.NotificationImpl;
import com.example.demo.Entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationImpl notificationImpl;

    /**
     * 产生新的通知
     * @param map 包含用户 ID 和通知内容的 Map
     * @return 响应结果
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createNewInform(@RequestBody Map<String, Object> map) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (map.containsKey("userID") && map.containsKey("content")) {
                String userID = (String) map.get("userID");
                String content = (String) map.get("content");
                notificationImpl.createNewInform(userID, content);
                response.put("message", "通知创建成功");
                response.put("success", true);
            } else {
                response.put("message", "请求体缺少必要参数");
                response.put("success", false);
            }
        } catch (Exception e) {
            response.put("message", "通知创建失败");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, response.get("success").equals(true) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 获取某个用户的所有通知
     * @param map 包含用户 ID 的 Map
     * @return 响应结果
     */
    @PostMapping("/getAll")
    public ResponseEntity<Map<String, Object>> getAllUserInform(@RequestBody Map<String, Object> map) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (map.containsKey("userId")) {
                String userId = (String) map.get("userId");
                List<Notification> notifications = notificationImpl.getAllUserInform(userId);
                response.put("message", "获取通知成功");
                response.put("success", true);
                response.put("notifications", notifications);
            } else {
                response.put("message", "请求体缺少用户 ID 参数");
                response.put("success", false);
            }
        } catch (Exception e) {
            response.put("message", "获取通知失败");
            response.put("success", false);
        }
        return new ResponseEntity<>(response, response.get("success").equals(true) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 将指定通知设置为已读
     * @param map 包含通知 ID 的 Map
     * @return 响应结果
     */
    @PostMapping("/markAsRead")
    public ResponseEntity<Map<String, Object>> markAsRead(@RequestBody Map<String, Object> map) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (map.containsKey("id")) {
                Integer id = (Integer) map.get("id");
                notificationImpl.updateIsRead(id, true);
                response.put("message", "通知已标记为已读");
                response.put("success", true);
            } else {
                response.put("message", "请求体缺少通知 ID 参数");
                response.put("success", false);
            }
        } catch (Exception e) {
            response.put("message", "标记通知为已读失败");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, response.get("success").equals(true) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}