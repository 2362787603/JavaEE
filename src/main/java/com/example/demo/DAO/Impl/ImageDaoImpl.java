package com.example.demo.DAO.Impl;

import com.example.demo.DAO.ImageDao;
import com.example.demo.Entity.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class ImageDaoImpl implements ImageDao {

    private static final Logger logger = LoggerFactory.getLogger(ImageDaoImpl.class);

    private final JdbcTemplate jdbc;

    public ImageDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public String getImagePathById(Integer imageId) {
        String sql = "SELECT image_url FROM image WHERE image_id = ?";
        return jdbc.queryForObject(sql, String.class, imageId);
    }

    @Override
    public Integer insertImage(String imagePath) {
        String sql = "INSERT INTO image (image_url) VALUES (?)";
        logger.info("开始插入图片路径，图片路径: {}, SQL: {}", imagePath, sql);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbc.update((PreparedStatementCreator) connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, imagePath);
                return ps;
            }, keyHolder);
            Integer imageId = keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
            logger.info("成功插入图片路径，图片 ID: {}", imageId);
            return imageId;
        } catch (Exception e) {
            logger.error("插入图片路径失败，图片路径: {}, 错误信息: {}", imagePath, e.getMessage(), e);
            return null;
        }
    }
}