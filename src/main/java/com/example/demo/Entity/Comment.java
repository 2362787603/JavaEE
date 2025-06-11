package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data               // 包含 getter、setter、toString
@AllArgsConstructor // 全参构造器
@NoArgsConstructor  // 无参构造器
public class Comment {
    private Integer Id;
    private String UserID;
    private Integer PostID;
    private Integer CommentID    = 0;
    private String  CommentContent;
    private Integer LikeNumber   = 0;
    private LocalDateTime CreateTime;
}
