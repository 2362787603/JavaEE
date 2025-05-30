package com.example.demo.Controller;

import com.example.demo.DAO.Impl.ForumFollowImpl;
import com.example.demo.DAO.Impl.ForumImpl;
import com.example.demo.Entity.Forum;
import com.example.demo.Entity.ForumFollow;

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

    @Autowired
    private ForumFollowImpl forumFollowImpl;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createForum(@RequestBody Map<String, Object> map) {
        logger.info("收到创建论坛请求，请求参数: {}", map);
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
                logger.info("论坛创建成功，论坛ID: {}", forumId);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "论坛创建失败");
                response.put("success", false);
                logger.warn("论坛创建失败，请求参数: {}", map);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("论坛创建出错，请求参数: {}, 错误信息: {}", map, e.getMessage(), e);
            response.put("message", "论坛创建过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Map<String, Object>> searchForum(@PathVariable Integer id) {
        logger.info("收到查询论坛请求，论坛ID: {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            Forum forum = forumImpl.searchForum(id);
            if (forum != null) {
                response.put("message", "论坛查询成功");
                response.put("success", true);
                response.put("forum", forum);
                logger.info("论坛查询成功，论坛ID: {}", id);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "未找到该论坛");
                response.put("success", false);
                logger.warn("未找到论坛，论坛ID: {}", id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("论坛查询出错，论坛ID: {}, 错误信息: {}", id, e.getMessage(), e);
            response.put("message", "论坛查询过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 删除论坛
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteForum(@PathVariable Integer id) {
        logger.info("收到删除论坛请求，论坛ID: {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            if (forumImpl.deleteForum(id)) {
                response.put("message", "论坛删除成功");
                response.put("success", true);
                logger.info("论坛删除成功，论坛ID: {}", id);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "论坛删除失败，未找到该论坛");
                response.put("success", false);
                logger.warn("论坛删除失败，未找到论坛ID: {}", id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("论坛删除出错，论坛ID: {}, 错误信息: {}", id, e.getMessage(), e);
            response.put("message", "论坛删除过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改论坛信息（原接口，可保留或根据需求决定是否移除）
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateForum(@PathVariable Integer id, @RequestBody Map<String, Object> map) {
        logger.info("收到修改论坛信息请求，论坛ID: {}, 请求参数: {}", id, map);
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
                logger.info("论坛信息修改成功，论坛ID: {}", id);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "论坛信息修改失败，未找到该论坛");
                response.put("success", false);
                logger.warn("论坛信息修改失败，未找到论坛ID: {}", id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("论坛信息修改出错，论坛ID: {}, 请求参数: {}, 错误信息: {}", id, map, e.getMessage(), e);
            response.put("message", "论坛信息修改过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改论坛名称
    @PutMapping("/{id}/name")
    public ResponseEntity<Map<String, Object>> updateForumName(@PathVariable Integer id, @RequestBody Map<String, Object> map) {
        logger.info("收到修改论坛名称请求，论坛ID: {}, 请求参数: {}", id, map);
        Map<String, Object> response = new HashMap<>();
        try {
            if (map.containsKey("name")) {
                String name = (String) map.get("name");
                boolean result = forumImpl.updateForumName(id, name);
                if (result) {
                    response.put("message", "论坛名称修改成功");
                    response.put("success", true);
                    logger.info("论坛名称修改成功，论坛ID: {}, 新名称: {}", id, name);
                } else {
                    response.put("message", "论坛名称修改失败，未找到该论坛");
                    response.put("success", false);
                    logger.warn("论坛名称修改失败，未找到论坛ID: {}", id);
                }
            } else {
                response.put("message", "请求体中缺少论坛名称");
                response.put("success", false);
                logger.warn("修改论坛名称请求缺少论坛名称，论坛ID: {}", id);
            }
            return new ResponseEntity<>(response, response.get("success").equals(true) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("论坛名称修改出错，论坛ID: {}, 请求参数: {}, 错误信息: {}", id, map, e.getMessage(), e);
            response.put("message", "论坛名称修改过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 修改论坛简介
    @PutMapping("/{id}/introduction")
    public ResponseEntity<Map<String, Object>> updateForumIntroduction(@PathVariable Integer id, @RequestBody Map<String, Object> map) {
        logger.info("收到修改论坛简介请求，论坛ID: {}, 请求参数: {}", id, map);
        Map<String, Object> response = new HashMap<>();
        try {
            if (map.containsKey("introduction")) {
                String introduction = (String) map.get("introduction");
                boolean result = forumImpl.updateForumIntroduction(id, introduction);
                if (result) {
                    response.put("message", "论坛简介修改成功");
                    response.put("success", true);
                    logger.info("论坛简介修改成功，论坛ID: {}, 新简介: {}", id, introduction);
                } else {
                    response.put("message", "论坛简介修改失败，未找到该论坛");
                    response.put("success", false);
                    logger.warn("论坛简介修改失败，未找到论坛ID: {}", id);
                }
            } else {
                response.put("message", "请求体中缺少论坛简介");
                response.put("success", false);
                logger.warn("修改论坛简介请求缺少论坛简介，论坛ID: {}", id);
            }
            return new ResponseEntity<>(response, response.get("success").equals(true) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("论坛简介修改出错，论坛ID: {}, 请求参数: {}, 错误信息: {}", id, map, e.getMessage(), e);
            response.put("message", "论坛简介修改过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查看论坛关注计数
     * @param id 论坛 ID
     * @return 响应结果
     */
    @GetMapping("/{id}/followCount")
    public ResponseEntity<Map<String, Object>> getFollowCount(@PathVariable Integer id) {
        logger.info("收到查看论坛关注计数请求，论坛ID: {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            Integer count = forumImpl.getFollowCount(id);
            if (count != null) {
                response.put("message", "获取关注计数成功");
                response.put("success", true);
                response.put("followCount", count);
                logger.info("获取论坛关注计数成功，论坛ID: {}, 关注数: {}", id, count);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "获取关注计数失败，未找到该论坛");
                response.put("success", false);
                logger.warn("获取论坛关注计数失败，未找到论坛ID: {}", id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("查看论坛关注计数出错，论坛ID: {}, 错误信息: {}", id, e.getMessage(), e);
            response.put("message", "查看论坛关注计数过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/follow")
    public ResponseEntity<Map<String, Object>> addFollow(@RequestBody Map<String, Object> map) {
        String userId = (String) map.get("userId");
        Integer forumId = (Integer) map.get("forumId");
        logger.info("收到添加关注请求，用户ID: {}, 论坛ID: {}", userId, forumId);
        Map<String, Object> response = new HashMap<>();
        try {
            ForumFollow follow = new ForumFollow();
            follow.setUserId(userId);
            follow.setForumId(forumId);
            Integer followId = forumFollowImpl.addFollow(follow);
            if (followId != null) {
                if (forumImpl.increaseFollowCount(follow.getForumId())) {
                    response.put("message", "关注成功，关注计数已更新");
                    response.put("success", true);
                    response.put("followId", followId);
                    logger.info("用户 {} 关注论坛 {} 成功，关注计数已更新，关注ID: {}", follow.getUserId(), follow.getForumId(), followId);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("message", "关注成功，但关注计数更新失败");
                    response.put("success", false);
                    response.put("followId", followId);
                    logger.warn("用户 {} 关注论坛 {} 成功，但关注计数更新失败，关注ID: {}", follow.getUserId(), follow.getForumId(), followId);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                response.put("message", "关注失败");
                response.put("success", false);
                logger.warn("用户 {} 关注论坛 {} 失败", follow.getUserId(), follow.getForumId());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("添加关注出错，用户ID: {}, 论坛ID: {}, 错误信息: {}", userId, forumId, e.getMessage(), e);
            response.put("message", "添加关注过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/follow")
    public ResponseEntity<Map<String, Object>> removeFollow(@RequestBody Map<String, Object> map) {
        String userId = (String) map.get("userId");
        Integer forumId = (Integer) map.get("forumId");
        logger.info("收到取消关注请求，用户ID: {}, 论坛ID: {}", userId, forumId);
        Map<String, Object> response = new HashMap<>();
        try {
            ForumFollow follow = new ForumFollow();
            follow.setUserId(userId);
            follow.setForumId(forumId);
            if (forumFollowImpl.removeFollow(follow)) {
                if (forumImpl.decreaseFollowCount(follow.getForumId())) {
                    response.put("message", "取消关注成功，关注计数已更新");
                    response.put("success", true);
                    logger.info("用户 {} 取消关注论坛 {} 成功，关注计数已更新", follow.getUserId(), follow.getForumId());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("message", "取消关注成功，但关注计数更新失败");
                    response.put("success", false);
                    logger.warn("用户 {} 取消关注论坛 {} 成功，但关注计数更新失败", follow.getUserId(), follow.getForumId());
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                response.put("message", "取消关注失败");
                response.put("success", false);
                logger.warn("用户 {} 取消关注论坛 {} 失败", follow.getUserId(), follow.getForumId());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("取消关注出错，用户ID: {}, 论坛ID: {}, 错误信息: {}", userId, forumId, e.getMessage(), e);
            response.put("message", "取消关注过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}