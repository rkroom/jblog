package com.example.demo.service.Impl;


import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
