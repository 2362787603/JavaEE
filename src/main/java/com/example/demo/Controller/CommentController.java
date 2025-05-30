package com.example.demo.Controller;

import com.example.demo.DAO.CommentDao;
import com.example.demo.Entity.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 评论相关接口
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentDao commentDao;

    /**
     * 创建新评论
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createNewComment(@RequestBody Map<String, Object> map) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Comment c = new Comment();
            c.setUserID((Integer) map.get("userID"));
            c.setPostID((Integer) map.get("postID"));
            c.setCommentID((Integer) map.get("commentID"));
            c.setCommentContent((String) map.get("commentContent"));

            int rows = commentDao.createNewComment(c);
            if (rows > 0) {
                resp.put("message", "评论成功");
                resp.put("success", true);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                resp.put("message", "评论失败");
                resp.put("success", false);
                return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("创建评论出错: {}", e.getMessage(), e);
            resp.put("message", "创建评论过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取某帖子的所有直接评论 (commentID == 0)
     */
    @GetMapping("/post/{postID}")
    public ResponseEntity<Map<String, Object>> getAllCommentUnderPost(@PathVariable Integer postID) {
        Map<String, Object> resp = new HashMap<>();
        try {
            List<Comment> list = commentDao.getAllCommentUnderPost(postID);
            resp.put("comments", list);
            resp.put("success", true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("查询评论出错: {}", e.getMessage(), e);
            resp.put("message", "查询评论过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取某评论的所有回复
     */
    @GetMapping("/reply/{commentID}")
    public ResponseEntity<Map<String, Object>> getAllReplyUnderComment(@PathVariable Integer commentID) {
        Map<String, Object> resp = new HashMap<>();
        try {
            List<Comment> list = commentDao.getAllReplyUnderComment(commentID);
            resp.put("replies", list);
            resp.put("success", true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("查询回复出错: {}", e.getMessage(), e);
            resp.put("message", "查询回复过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 评论点赞
     */
    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> likeComment(@RequestBody Map<String, Object> map) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Integer commentID = (Integer) map.get("commentID");
            int rows = commentDao.likeComment(commentID);
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
            logger.error("评论点赞出错: {}", e.getMessage(), e);
            resp.put("message", "点赞过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
