package com.example.demo.DAO.Impl;

import com.example.demo.DAO.PostImageMappingDao;
import com.example.demo.Entity.PostImageMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PostImageMappingDaoImpl implements PostImageMappingDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<PostImageMapping> mapper = new RowMapper<>() {
        @Override
        public PostImageMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
            PostImageMapping mapping = new PostImageMapping();
            mapping.setPostId(rs.getInt("post_id"));
            mapping.setImageId(rs.getInt("image_id"));
            return mapping;
        }
    };

    @Override
    public int addMapping(Integer postId, Integer imageId) {
        String sql = "INSERT INTO post_image (post_id, image_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, postId,imageId);
    }

    @Override
    public List<PostImageMapping> getMappingsByPostId(Integer postId) {
        String sql = "SELECT * FROM post_image WHERE post_id = ?";
        return jdbcTemplate.query(sql, mapper, postId);
    }

    @Override
    public int deleteMappingByPostId(Integer postId) {
        String sql = "DELETE FROM post_image WHERE post_id = ?";
        return jdbcTemplate.update(sql, postId);
    }
}