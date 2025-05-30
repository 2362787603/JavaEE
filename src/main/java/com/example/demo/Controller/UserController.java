package com.example.demo.Controller;

import com.example.demo.DAO.Impl.UserImpl;
import com.example.demo.Entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserImpl userImpl;

    // 假设头像存储的基础目录
    private static final String AVATAR_BASE_DIR = "uploads/avatars";

    // 已有注册接口
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> map) {
        logger.info("收到用户注册请求，请求参数: {}", map);
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
                logger.info("用户 {} 注册成功", user.getUserId());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "注册失败");
                response.put("success", false);
                logger.warn("用户 {} 注册失败", user.getUserId());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("用户注册出错，请求参数: {}, 错误信息: {}", map, e.getMessage(), e);
            response.put("message", "注册过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 已有登录接口
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> map) {
        logger.info("收到用户登录请求，请求参数: {}", map);
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
                userInfo.put("admin", user.getAdmin());

                response.put("message", "登录成功");
                response.put("success", true);
                response.put("user", userInfo);
                logger.info("用户 {} 登录成功", userid);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "用户名或密码错误");
                response.put("success", false);
                logger.warn("用户 {} 登录失败，用户名或密码错误", userid);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            logger.error("用户登录出错，请求参数: {}, 错误信息: {}", map, e.getMessage(), e);
            response.put("message", "登录过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 查询所有用户
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userImpl.allUser();
            response.put("message", "查询成功");
            response.put("success", true);
            response.put("users", users.stream().map(user -> {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", user.getUserId());
                userInfo.put("username", user.getUsername());
                userInfo.put("level", user.getLevel());
                userInfo.put("email", user.getEmail());
                userInfo.put("admin", user.getAdmin());
                return userInfo;
            }).toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("查询所有用户出错: {}", e.getMessage());
            response.put("message", "查询所有用户发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 根据 ID 查询用户
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userImpl.findById(id);
            if (!users.isEmpty()) {
                User user = users.get(0);
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", user.getUserId());
                userInfo.put("username", user.getUsername());
                userInfo.put("level", user.getLevel());
                userInfo.put("email", user.getEmail());
                userInfo.put("admin", user.getAdmin());
                response.put("message", "查询成功");
                response.put("success", true);
                response.put("user", userInfo);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "未找到该用户");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("根据 ID 查询用户出错: {}", e.getMessage());
            response.put("message", "根据 ID 查询用户发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userImpl.deleteById(id)) {
                response.put("message", "删除成功");
                response.put("success", true);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "删除失败，未找到该用户");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("删除用户出错: {}", e.getMessage());
            response.put("message", "删除用户发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改用户名
    @PutMapping("/{id}/username")
    public ResponseEntity<Map<String, Object>> updateUsername(@PathVariable String id, @RequestBody String username) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userImpl.findById(id);
            if (!users.isEmpty()) {
                User user = users.get(0);
                user.setUsername(username);
                if (userImpl.updateById(user)) {
                    response.put("message", "用户名修改成功");
                    response.put("success", true);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("message", "用户名修改失败");
                    response.put("success", false);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                response.put("message", "未找到该用户");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("修改用户名出错: {}", e.getMessage());
            response.put("message", "修改用户名发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改密码
    @PutMapping("/{id}/password")
    public ResponseEntity<Map<String, Object>> updatePassword(@PathVariable String id, @RequestBody String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userImpl.findById(id);
            if (!users.isEmpty()) {
                User user = users.get(0);
                user.setPassword(password);
                if (userImpl.updateById(user)) {
                    response.put("message", "密码修改成功");
                    response.put("success", true);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("message", "密码修改失败");
                    response.put("success", false);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                response.put("message", "未找到该用户");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("修改密码出错: {}", e.getMessage());
            response.put("message", "修改密码发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改等级
    @PutMapping("/{id}/level")
    public ResponseEntity<Map<String, Object>> updateLevel(@PathVariable String id, @RequestBody int level) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userImpl.findById(id);
            if (!users.isEmpty()) {
                User user = users.get(0);
                user.setLevel(level);
                if (userImpl.updateById(user)) {
                    response.put("message", "等级修改成功");
                    response.put("success", true);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("message", "等级修改失败");
                    response.put("success", false);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                response.put("message", "未找到该用户");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("修改等级出错: {}", e.getMessage());
            response.put("message", "修改等级发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改邮箱
    @PutMapping("/{id}/email")
    public ResponseEntity<Map<String, Object>> updateEmail(@PathVariable String id, @RequestBody String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userImpl.findById(id);
            if (!users.isEmpty()) {
                User user = users.get(0);
                user.setEmail(email);
                if (userImpl.updateById(user)) {
                    response.put("message", "邮箱修改成功");
                    response.put("success", true);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("message", "邮箱修改失败");
                    response.put("success", false);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                response.put("message", "未找到该用户");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("修改邮箱出错: {}", e.getMessage());
            response.put("message", "修改邮箱发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改管理员状态
    @PutMapping("/{id}/admin")
    public ResponseEntity<Map<String, Object>> updateAdmin(@PathVariable String id, @RequestBody int admin) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userImpl.findById(id);
            if (!users.isEmpty()) {
                User user = users.get(0);
                user.setAdmin(admin);
                if (userImpl.updateById(user)) {
                    response.put("message", "管理员状态修改成功");
                    response.put("success", true);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("message", "管理员状态修改失败");
                    response.put("success", false);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                response.put("message", "未找到该用户");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("修改管理员状态出错: {}", e.getMessage());
            response.put("message", "修改管理员状态发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 添加头像
    @PostMapping("/{id}/avatar")
    public ResponseEntity<Map<String, Object>> addAvatar(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userImpl.findUserById(id);
            if (user == null) {
                response.put("message", "未找到该用户");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (file.isEmpty()) {
                response.put("message", "上传的文件为空");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 创建存储目录
            File dir = new File(AVATAR_BASE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String fileName = id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(AVATAR_BASE_DIR, fileName);

            // 保存文件
            file.transferTo(filePath);

            // 更新用户头像路径
            user.setAvatarPath(filePath.toString());
            if (userImpl.updateById(user)) {
                response.put("message", "头像添加成功");
                response.put("success", true);
                response.put("avatarPath", filePath.toString());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // 删除已保存的文件
                Files.deleteIfExists(filePath);
                response.put("message", "头像添加失败");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            logger.error("添加头像出错: {}", e.getMessage());
            response.put("message", "添加头像发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 更换头像
    @PutMapping("/{id}/avatar")
    public ResponseEntity<Map<String, Object>> updateAvatar(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userImpl.findUserById(id);
            if (user == null) {
                response.put("message", "未找到该用户");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (file.isEmpty()) {
                response.put("message", "上传的文件为空");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 删除旧头像
            String oldAvatarPath = user.getAvatarPath();
            if (oldAvatarPath != null) {
                File oldAvatarFile = new File(oldAvatarPath);
                if (oldAvatarFile.exists()) {
                    oldAvatarFile.delete();
                }
            }

            // 创建存储目录
            File dir = new File(AVATAR_BASE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String fileName = id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(AVATAR_BASE_DIR, fileName);

            // 保存新文件
            file.transferTo(filePath);

            // 更新用户头像路径
            user.setAvatarPath(filePath.toString());
            if (userImpl.updateById(user)) {
                response.put("message", "头像更换成功");
                response.put("success", true);
                response.put("avatarPath", filePath.toString());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // 删除已保存的新文件
                Files.deleteIfExists(filePath);
                response.put("message", "头像更换失败");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            logger.error("更换头像出错: {}", e.getMessage());
            response.put("message", "更换头像发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 加载头像
    @GetMapping("/{id}/avatar")
    public ResponseEntity<Resource> loadAvatar(@PathVariable String id) {
        User user = userImpl.findUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String avatarPath = user.getAvatarPath();
        if (avatarPath == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        File file = new File(avatarPath);
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
