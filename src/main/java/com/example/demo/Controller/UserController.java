package com.example.demo.Controller;

import com.example.demo.DAO.Impl.UserImpl;
import com.example.demo.Entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserImpl userImpl;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> map) {
        Map<String, Object> response = new HashMap<>();
        User user = new User();
        if (map.containsKey("userid")) {
            user.setUserId((String) map.get("userid"));
        }
        if (map.containsKey("username")) {
            user.setUsername((String) map.get("username"));
        }
        if (map.containsKey("password")) {
            user.setPassword((String) map.get("password"));
        }
        try {
            if (userImpl.addUser(user)) {
                response.put("message", "注册成功");
                response.put("success", true);
                // 不返回用户对象，防止密码泄露
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "注册失败");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("用户注册出错: {}", e.getMessage());
            response.put("message", "注册过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> map) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userid = (String) map.get("userid");
            String password = (String) map.get("password");
            User user = userImpl.login(userid, password);
            if (user != null) {
                // 创建新的 Map 存储不含密码的用户信息
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", user.getUserId());
                userInfo.put("username", user.getUsername());
                userInfo.put("level", user.getLevel());
                userInfo.put("email", user.getEmail());
                userInfo.put("imageId", user.getImageId());
                userInfo.put("admin", user.getAdmin());

                response.put("message", "登录成功");
                response.put("success", true);
                response.put("user", userInfo);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "用户名或密码错误");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            logger.error("用户登录出错: {}", e.getMessage());
            response.put("message", "登录过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
