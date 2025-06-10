package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data               // 包含 getter、setter、toString
@AllArgsConstructor // 全参构造器
@NoArgsConstructor  // 无参构造器
public class Post {
    private Integer Id;
    private Integer UserID;
    private Integer ForumID;
    private String  Title;
    private String  Content;
    private Integer LikeNumber  = 0;
    private LocalDateTime CreateTime;
    private String  UserName;
}
