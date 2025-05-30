package com.example.demo.Controller;

import com.example.demo.DAO.PostDao;
import com.example.demo.Entity.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 帖子相关接口
 */
@RestController
@RequestMapping("/post")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostDao postDao;


    @GetMapping("/test-db")
    public String testDb() {
        return postDao.testConnection();
    }


    /**
     * 创建新帖
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createNewPost(@RequestBody Map<String, Object> map) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Post post = new Post();
            post.setUserID((Integer) map.get("userID"));
            post.setForumID((Integer) map.get("forumID"));
            post.setTitle((String) map.get("title"));
            post.setContent((String) map.get("content"));

            int rows = postDao.createNewPost(post);
            if (rows > 0) {
                resp.put("message", "创建成功");
                resp.put("success", true);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                resp.put("message", "创建失败");
                resp.put("success", false);
                return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("创建帖子出错: {}", e.getMessage(), e);
            resp.put("message", "创建帖子过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据帖子ID查询单条
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPost(@PathVariable Integer id) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Post post = postDao.getPost(id);
            if (post != null) {
                resp.put("post", post);
                resp.put("success", true);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                resp.put("message", "未找到该帖子");
                resp.put("success", false);
                return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("查询帖子出错: {}", e.getMessage(), e);
            resp.put("message", "查询帖子过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询某个论坛下的所有帖子
     */
    @GetMapping("/forum/{forumID}")
    public ResponseEntity<Map<String, Object>> getAllPostInForum(@PathVariable Integer forumID) {
        Map<String, Object> resp = new HashMap<>();
        try {
            List<Post> list = postDao.getAllPostInForum(forumID);
            resp.put("posts", list);
            resp.put("success", true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("查询论坛帖子出错: {}", e.getMessage(), e);
            resp.put("message", "查询论坛帖子过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询某个用户的所有帖子
     */
    @GetMapping("/user/{userID}")
    public ResponseEntity<Map<String, Object>> getAllUserPost(@PathVariable Integer userID) {
        Map<String, Object> resp = new HashMap<>();
        try {
            List<Post> list = postDao.getAllUserPost(userID);
            resp.put("posts", list);
            resp.put("success", true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("查询用户帖子出错: {}", e.getMessage(), e);
            resp.put("message", "查询用户帖子过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 帖子点赞
     */
    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> likePost(@RequestBody Map<String, Object> map) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Integer postID = (Integer) map.get("postID");
            int rows = postDao.likePost(postID);
            if (rows > 0) {
                resp.put("message", "点赞成功");
                resp.put("success", true);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                resp.put("message", "点赞失败");
                resp.put("success", false);
                return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("点赞出错: {}", e.getMessage(), e);
            resp.put("message", "点赞过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
