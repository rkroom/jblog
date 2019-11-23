package com.rkroom.blog.service;

import com.rkroom.blog.entity.User;

public interface UserService { //UserService接口
    User selectByUsername(String username); //一个根据用户名查询用户信息的接口
    User selectById(int id);
    int updatePasswordById(String password,int id);
    void insertUser(User user);
}
