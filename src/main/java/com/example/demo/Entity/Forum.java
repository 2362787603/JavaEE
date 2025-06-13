package com.example.demo.Entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor  
public class Forum {
    private Integer id;
    private String userID;
    private String name;
    private String introduction;
    private Integer followCount;
    private Integer postCount; // 新增帖子数量字段

    private Integer imageId;
}