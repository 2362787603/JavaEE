package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //包含了get，set和toString
@AllArgsConstructor //有参构造器 set
@NoArgsConstructor  //无参构造器 get

public class User {
    private String UserId;
    private String Username;
	private String Password;
    private int Level=0;
    
    private String Email;
    private String ImageId;
    private int Admin;
}
