package com.example.demo.Controller;

import com.example.demo.DAO.Impl.ForumFollowImpl;
import com.example.demo.DAO.Impl.ForumImpl;
import com.example.demo.DAO.LikeDao;
import com.example.demo.Entity.Forum;
import com.example.demo.Entity.ForumFollow;
import com.example.demo.Entity.Like;
import com.example.demo.Entity.Post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/like")
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(ForumController.class);

    @Autowired
    private LikeDao likeDao;

    /**
     * 判断用户是否点赞某个帖子
     */
    @PostMapping("/getUserLikePost")
    public ResponseEntity<Map<String, Object>> getUserLikePost(@RequestBody Map<String, Object> map) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String userID = (String) map.get("userID");
            Integer postID = (Integer) map.get("postID");
            Boolean isUserLikePost = likeDao.getUserLikePost(userID,postID);
            resp.put("isUserLike",isUserLikePost);
            resp.put("message", "创建成功");
            resp.put("success", true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("查看用户点赞出错: {}", e.getMessage(), e);
            resp.put("message", "查看用户点赞过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 判断用户是否点赞评论
     */
    @PostMapping("/getUserLikeComment")
    public ResponseEntity<Map<String, Object>> getUserLikeComment(@RequestBody Map<String, Object> map) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String userID = (String) map.get("userID");
            Integer commentID = (Integer) map.get("commentID");
            Boolean isUserLikePost = likeDao.getUserLikeComment(userID,commentID);
            resp.put("isUserLike",isUserLikePost);
            resp.put("message", "创建成功");
            resp.put("success", true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("查看用户点赞出错: {}", e.getMessage(), e);
            resp.put("message", "查看用户点赞过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 判断用户是否点赞评论
     */
    @PostMapping("/getAllUserLike")
    public ResponseEntity<Map<String, Object>> getAllUserLike(@RequestBody Map<String, Object> map) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String userID = (String) map.get("userID");
            List<Like> likeList = likeDao.getAllLikes(userID);
            resp.put("likeList",likeList);
            resp.put("message", "创建成功");
            resp.put("success", true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("查看用户点赞出错: {}", e.getMessage(), e);
            resp.put("message", "查看用户点赞过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}