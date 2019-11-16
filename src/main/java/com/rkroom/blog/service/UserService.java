package com.rkroom.blog.service;

import com.rkroom.blog.entity.User;

public interface UserService { //UserService接口
    public User selectByUsername(String username); //一个根据用户名查询用户信息的接口
    public int updatePasswordByUsername(String password,String username); //根据用户名更新密码
    public User selectById(int id);
}
