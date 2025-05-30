package com.example.demo.Controller;

import com.example.demo.DAO.Impl.ForumImpl;
import com.example.demo.Entity.Forum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/forum")
public class ForumController {

    private static final Logger logger = LoggerFactory.getLogger(ForumController.class);

    @Autowired
    private ForumImpl forumImpl;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createForum(@RequestBody Map<String, Object> map) {
        Map<String, Object> response = new HashMap<>();
        try {
            Forum forum = new Forum();
            if (map.containsKey("userID")) {
                forum.setUserID((String) map.get("userID"));
            }
            if (map.containsKey("name")) {
                forum.setName((String) map.get("name"));
            }
            if (map.containsKey("introduction")) {
                forum.setIntroduction((String) map.get("introduction"));
            }

            Integer forumId = forumImpl.createForum(forum.getUserID(), forum.getName(), forum.getIntroduction());
            if (forumId != null) {
                response.put("message", "论坛创建成功");
                response.put("success", true);
                response.put("forumId", forumId);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "论坛创建失败");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("论坛创建出错: {}", e.getMessage());
            response.put("message", "论坛创建过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Map<String, Object>> searchForum(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Forum forum = forumImpl.searchForum(id);
            if (forum != null) {
                response.put("message", "论坛查询成功");
                response.put("success", true);
                response.put("forum", forum);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "未找到该论坛");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("论坛查询出错: {}", e.getMessage());
            response.put("message", "论坛查询过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 删除论坛
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteForum(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (forumImpl.deleteForum(id)) {
                response.put("message", "论坛删除成功");
                response.put("success", true);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "论坛删除失败，未找到该论坛");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("论坛删除出错: {}", e.getMessage());
            response.put("message", "论坛删除过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改论坛信息（原接口，可保留或根据需求决定是否移除）
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateForum(@PathVariable Integer id, @RequestBody Map<String, Object> map) {
        Map<String, Object> response = new HashMap<>();
        try {
            Forum forum = new Forum();
            if (map.containsKey("userID")) {
                forum.setUserID((String) map.get("userID"));
            }
            if (map.containsKey("name")) {
                forum.setName((String) map.get("name"));
            }
            if (map.containsKey("introduction")) {
                forum.setIntroduction((String) map.get("introduction"));
            }

            boolean result = forumImpl.updateForum(id, forum.getName(), forum.getIntroduction());
            if (result) {
                response.put("message", "论坛信息修改成功");
                response.put("success", true);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "论坛信息修改失败，未找到该论坛");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("论坛信息修改出错: {}", e.getMessage());
            response.put("message", "论坛信息修改过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改论坛名称
    @PutMapping("/{id}/name")
    public ResponseEntity<Map<String, Object>> updateForumName(@PathVariable Integer id, @RequestBody Map<String, Object> map) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (map.containsKey("name")) {
                String name = (String) map.get("name");
                boolean result = forumImpl.updateForumName(id, name);
                if (result) {
                    response.put("message", "论坛名称修改成功");
                    response.put("success", true);
                } else {
                    response.put("message", "论坛名称修改失败，未找到该论坛");
                    response.put("success", false);
                }
            } else {
                response.put("message", "请求体中缺少论坛名称");
                response.put("success", false);
            }
            return new ResponseEntity<>(response, response.get("success").equals(true) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("论坛名称修改出错: {}", e.getMessage());
            response.put("message", "论坛名称修改过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改论坛简介
    @PutMapping("/{id}/introduction")
    public ResponseEntity<Map<String, Object>> updateForumIntroduction(@PathVariable Integer id, @RequestBody Map<String, Object> map) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (map.containsKey("introduction")) {
                String introduction = (String) map.get("introduction");
                boolean result = forumImpl.updateForumIntroduction(id, introduction);
                if (result) {
                    response.put("message", "论坛简介修改成功");
                    response.put("success", true);
                } else {
                    response.put("message", "论坛简介修改失败，未找到该论坛");
                    response.put("success", false);
                }
            } else {
                response.put("message", "请求体中缺少论坛简介");
                response.put("success", false);
            }
            return new ResponseEntity<>(response, response.get("success").equals(true) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("论坛简介修改出错: {}", e.getMessage());
            response.put("message", "论坛简介修改过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}