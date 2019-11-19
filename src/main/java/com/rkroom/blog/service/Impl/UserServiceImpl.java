package com.rkroom.blog.service.Impl;


import com.rkroom.blog.entity.User;
import com.rkroom.blog.service.UserService;
import com.rkroom.blog.repository.UserRepository;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //定义为服务
public class UserServiceImpl implements UserService { //实现userservice接口
    @Autowired //自动装配
    UserRepository userRepository;
    public User selectByUsername(String username){
        return userRepository.findByUsername(username); //根据用户名返回数据
    }

    public int updatePasswordByUsername(String password,String username){
        userRepository.updatePasswordByUsername(password,username);
        return 1;
    }
    public User selectById(int id){
        Optional<User> optional = userRepository.findById(id);
        User user = optional.get();
        return user;
    }

    public int updatePasswordById(String password,int id){
        String username = userRepository.findUsernameById(id);
        //将密码hash后存入数据库
        password = new SimpleHash("md5",password, ByteSource.Util.bytes(username),2).toString();
        userRepository.updatePasswordById(password,id);
        return 1;
    }
}
