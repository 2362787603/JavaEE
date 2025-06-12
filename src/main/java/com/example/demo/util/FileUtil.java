package com.example.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtil {
    // 假设图片存储的基础目录
    private static final String IMAGE_BASE_DIR = "uploads/images";

    /**
     * 上传文件
     * @param file 要上传的文件
     * @return 上传后的文件名
     * @throws IOException 上传过程中出现的 IO 异常
     */
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        // 创建存储目录
        File dir = new File(IMAGE_BASE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成唯一文件名
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(IMAGE_BASE_DIR, fileName);
        // 保存文件
        file.transferTo(filePath);
        return fileName;
    }

    /**
     * 获取上传路径
     * @return 上传路径
     */
    public String getUploadPath() {
        return IMAGE_BASE_DIR;
    }
}