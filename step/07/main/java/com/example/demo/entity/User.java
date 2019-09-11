package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity //实体注解
@Data  //自动生成set和get方法
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false,unique = true) //不能为空，不能重复
    private String username; //用户名
    @Column(nullable = false) //不能为空
    private String password; //密码
    @Column(nullable = false) //不能为空
    private String role; //角色
    @Column
    private String email; //邮件
    @Column
    private String nickname; //昵称
    @Column
    private String statement; //简介
    @Column(columnDefinition = "bool default false") //布尔类型，只有两种状态
    private boolean locked; //是否锁定
}