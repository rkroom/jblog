package com.rkroom.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity //实体注解
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler","password"})
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}