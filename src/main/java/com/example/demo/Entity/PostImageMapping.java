package com.example.demo.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostImageMapping {
    private Integer postId;
    private Integer imageId;
}