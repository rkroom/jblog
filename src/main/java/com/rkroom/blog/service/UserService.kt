package com.rkroom.blog.service

import com.rkroom.blog.entity.User

interface UserService {
    //UserService接口
    fun selectByUsername(username: String?): User? //一个根据用户名查询用户信息的接口

    fun selectById(id: Int): User?
    fun updatePasswordById(password: String?, id: Int): Int
    fun insertUser(user: User?)
}