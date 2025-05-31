package com.example.demo.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class Notification {
    private Integer id;
    private String userId;
    private String content;
    private boolean Read;
    private Date createTime; // 添加时间戳字段
}