package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
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

    @Override //重写doGetAuthenticationInfo方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String principal = (String) token.getPrincipal(); //获取传递过来的用户名
        User user = Optional.ofNullable(userService.selectByUsername(principal)).orElseThrow(UnknownAccountException::new); //获取对应user的相关信息
        if (user.isLocked()) { //如果用户呗锁定
            throw new LockedAccountException(); //抛出锁定信息
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal,user.getPassword(),getName()); //对比密码
        Session session = SecurityUtils.getSubject().getSession(); //获取session
        ((org.apache.shiro.session.Session) session).setAttribute("USER_SESSION", user); //设置session
        return authenticationInfo;
    }

    @Override //重写doGetAuthorizationInfo方法，doGetAuthorizationInfo方法主要是获取用户权限的，我们暂时不涉及这个方面的内容
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }
}