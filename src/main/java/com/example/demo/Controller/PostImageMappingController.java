package com.example.demo.Controller;

import com.example.demo.DAO.PostImageMappingDao;
import com.example.demo.Entity.PostImageMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/postImageMapping")
public class PostImageMappingController {

    private static final Logger logger = LoggerFactory.getLogger(PostImageMappingController.class);

    @Autowired
    private PostImageMappingDao postImageMappingDao;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addMapping(@RequestBody Map<String, Object> mappings) {
        Map<String, Object> response = new HashMap<>();
        int successCount = 0;
        try {
                Integer postId = (Integer) mappings.get("postId");
                Integer imageId = (Integer) mappings.get("imageId");
                if (postId != null && imageId != null) {
                    int result = postImageMappingDao.addMapping(postId, imageId);
                    if (result > 0) {
                        successCount++;
                    }
                }
            if (successCount > 0) {
                response.put("message", "成功添加 " + successCount + " 条映射关系");
                response.put("success", true);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "添加映射关系失败");
                response.put("success", false);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("添加映射关系出错: {}", e.getMessage(), e);
            response.put("message", "添加映射关系过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByPostId/{postId}")
    public ResponseEntity<Map<String, Object>> getMappingsByPostId(@PathVariable Integer postId) {
        // 添加日志记录开始获取映射关系的操作
        logger.info("开始根据帖子 ID {} 获取映射关系", postId);
        Map<String, Object> response = new HashMap<>();
        try {
            List<PostImageMapping> mappings = postImageMappingDao.getMappingsByPostId(postId);
            response.put("mappings", mappings);
            response.put("success", true);
            // 添加日志记录获取映射关系成功
            logger.info("根据帖子 ID {} 获取映射关系成功", postId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("根据帖子 ID 获取映射关系出错: {}", e.getMessage(), e);
            response.put("message", "根据帖子 ID 获取映射关系过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteByPostId/{postId}")
    public ResponseEntity<Map<String, Object>> deleteMappingByPostId(@PathVariable Integer postId) {
        // 添加日志记录开始删除映射关系的操作
        logger.info("开始根据帖子 ID {} 删除映射关系", postId);
        Map<String, Object> response = new HashMap<>();
        try {
            int result = postImageMappingDao.deleteMappingByPostId(postId);
            if (result > 0) {
                response.put("message", "删除映射关系成功");
                response.put("success", true);
                // 添加日志记录删除映射关系成功
                logger.info("根据帖子 ID {} 删除映射关系成功", postId);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "删除映射关系失败");
                response.put("success", false);
                // 添加日志记录删除映射关系失败
                logger.info("根据帖子 ID {} 删除映射关系失败", postId);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("根据帖子 ID 删除映射关系出错: {}", e.getMessage(), e);
            response.put("message", "根据帖子 ID 删除映射关系过程中发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}