package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService { //UserService接口
    public User selectByUsername(String username); //一个根据用户名查询用户信息的接口
}
