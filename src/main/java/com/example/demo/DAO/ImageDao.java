package com.example.demo.DAO;

import com.example.demo.Entity.Image;

public interface ImageDao {

    Integer insertImage(String imagePath);
    String getImagePathById(Integer imageId);
}