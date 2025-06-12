package com.example.demo.DAO;

import com.example.demo.Entity.PostImageMapping;
import java.util.List;

public interface PostImageMappingDao {
    int addMapping(Integer postId, Integer imageId);
    List<PostImageMapping> getMappingsByPostId(Integer postId);
    int deleteMappingByPostId(Integer postId);
}