package com.rkroom.blog.config;

import com.rkroom.blog.entity.User;
import com.rkroom.blog.service.UserService;
import com.rkroom.blog.utility.JWTUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;

@Component //把普通pojo实例化到spring容器中
@Configuration //定义为配置类
public class MyShiroRealm extends AuthorizingRealm { //继承自AuthorizingRealm

    @Autowired //自动装配User服务
    private UserService userService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof JWTToken || token instanceof UsernamePasswordToken){
            return true;
        }else {
            return false;
        }
    }

    @Override //重写doGetAuthenticationInfo方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
            //使用JWT的token进行登陆
            //获取token
            String authuser = (String) token.getCredentials();
            // 解密获得username，用于和数据库进行对比
            String username = JWTUtil.getUsername(authuser);
            // 如果用户名为空，则抛出错误
            if (username == null) {
                throw new AuthenticationException("token invalid");
            }
            //获取用户信息
            User user = Optional.ofNullable(userService.selectByUsername(username)).orElseThrow(UnknownAccountException::new);
            if (user.isLocked()) { //如果用户被锁定
                throw new LockedAccountException(); //抛出锁定信息
            }
            // 如果验证为通过则提示用户名或密码错误
            if (!JWTUtil.verify(authuser, username, user.getPassword())) {
                throw new AuthenticationException("Username or password error");
            }
            //返回验证信息
            return new SimpleAuthenticationInfo(authuser, authuser, "my_realm");
    }

    @Override //重写doGetAuthorizationInfo方法，doGetAuthorizationInfo方法主要是获取用户权限的，我们暂时不涉及这个方面的内容
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }
}