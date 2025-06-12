package com.example.demo.Controller;

import com.example.demo.DAO.Impl.ImageDaoImpl;
import com.example.demo.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/image")
public class ImageController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private ImageDaoImpl imageDaoimpl;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> addImage(@RequestParam("file") MultipartFile file) {
        logger.info("开始执行 addImage 方法，尝试处理文件上传，文件名: {}", file.getOriginalFilename());
        Map<String, Object> response = new HashMap<>();
        try {
            if (file.isEmpty()) {
                response.put("message", "上传的文件为空");
                response.put("success", false);
                logger.warn("文件上传失败，原因为上传的文件为空，文件名: {}", file.getOriginalFilename());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            // 上传文件
            String fileName = fileUtil.uploadFile(file);
            // 构建完整的图片路径，这里假设 uploadPath 是 FileUtil 中的上传路径
            String imagePath = fileUtil.getUploadPath() + java.io.File.separator + fileName; 
            // 将图片信息插入数据库
            int imageId = imageDaoimpl.insertImage(imagePath);

            response.put("message", "图片添加成功");
            response.put("success", true);
            response.put("fileName", fileName);
            response.put("imageId", imageId); // 返回插入后的图片 ID
            logger.info("图片添加成功，文件名: {}, 图片 ID: {}", fileName, imageId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("添加图片出错: {}", e.getMessage());
            response.put("message", "添加图片发生错误");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/downloadById/{imageId}")
    public ResponseEntity<byte[]> downloadImageById(@PathVariable Integer imageId) {
        try {
            // 根据图片 ID 查询图片路径
            String imagePath = imageDaoimpl.getImagePathById(imageId);
            if (imagePath == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
            // 对文件名进行编码
            String fileName = path.getFileName().toString();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            headers.setContentDispositionFormData("attachment", encodedFileName);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("下载图片出错: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}