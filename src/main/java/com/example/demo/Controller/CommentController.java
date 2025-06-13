package com.example.demo.Controller;

import com.example.demo.DAO.CommentDao;
import com.example.demo.DAO.LikeDao;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Like;
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

    @Autowired
    private LikeDao likeDao;
    /**
     * 创建新评论
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createNewComment(@RequestBody Map<String, Object> map) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Comment c = new Comment();
            c.setUserID((String) map.get("userID"));
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
            Integer commentID = (Integer) map.get("ID");
            String userID = (String) map.get("userID");
            int rows = commentDao.likeComment(commentID);
            if (rows > 0) {
                resp.put("message", "点赞成功");
                resp.put("success", true);
                likeDao.createNewLike(new Like(userID,0,commentID));
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

    /**
     * 取消评论点赞
     */
    @PostMapping("/cancelLike")
    public ResponseEntity<Map<String, Object>> cancelLikeComment(@RequestBody Map<String, Object> map) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Integer commentID = (Integer) map.get("ID");
            String userID = (String) map.get("userID");
            int rows = commentDao.cancelLikeComment(commentID);
            if (rows > 0) {
                resp.put("message", "点赞成功");
                resp.put("success", true);
                likeDao.deleteUserLike(new Like(userID,0,commentID));
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

    /**
     * 获取某用户的所有评论
     * （可以用 PathVariable 或者从请求头/Token 中读取当前 userID）
     */
    @GetMapping("/user/{userID}")
    public ResponseEntity<Map<String, Object>> getAllCommentsByUser(@PathVariable String userID) {
        Map<String, Object> resp = new HashMap<>();
        try {
            List<Comment> list = commentDao.getAllCommentsByUser(userID);
            resp.put("comments", list);
            resp.put("success", true);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("获取用户评论出错: {}", e.getMessage(), e);
            resp.put("message", "查询过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> getCommentById(@PathVariable Integer commentId) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Comment comment = commentDao.getCommentById(commentId);
            if (comment != null) {
                resp.put("comment", comment);
                resp.put("success", true);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                resp.put("message", "未找到该评论");
                resp.put("success", false);
                return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("根据评论 ID 获取评论出错: {}", e.getMessage(), e);
            resp.put("message", "根据评论 ID 获取评论过程中发生错误");
            resp.put("success", false);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
