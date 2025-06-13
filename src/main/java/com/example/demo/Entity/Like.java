
package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //包含了get，set和toString
@AllArgsConstructor //有参构造器 set
@NoArgsConstructor  //无参构造器 get
public class Like{
    private Integer id;
    private String userId;
    private Integer postId;
    private Integer commentId;

    public Integer getId(){return this.id;}
    public String getUserId(){return this.userId;}
    public Integer getPostId(){return this.postId;}
    public Integer getCommentId(){return this.commentId;}

    public Like(String userId,Integer postId,Integer commentId){
        this.userId = userId;this.postId = postId;this.commentId = commentId;
    }
}
