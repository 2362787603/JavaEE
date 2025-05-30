package com.example.demo.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForumFollow {
    private Integer id;
    private String userId;
    private Integer forumId;
}