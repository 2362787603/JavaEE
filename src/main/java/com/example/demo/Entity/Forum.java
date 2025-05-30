package com.example.demo.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Forum {
    private Integer id;
    private String userID;
    private String name;
    private String introduction;
    private Integer followCount; // 新增关注计数字段
}