package com.example.demo.control;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.spring.config.ShiroConfiguration;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@RestController
@RequestMapping("/login") //匹配/login
public class LoginControl {

    private static final Logger log = LoggerFactory.getLogger(ShiroConfiguration.class); //设置log

    @GetMapping("") //GET方法匹配/login
    public ModelAndView login(){
        ModelAndView login = new ModelAndView();
        login.setViewName("login"); //设置登陆页面为login.html
        return login;
    }

    @PostMapping("") //POST方法匹配/login
    public ModelAndView login(String username, String password,boolean rememberMe,RedirectAttributes model) {
        Subject sub = SecurityUtils.getSubject(); //获取登陆信息
        //将密码hash,hash方法选为md5，同时加盐，盐为用户名，hash次数为两次，将结果转换为String类型
        password = new SimpleHash("md5",password, ByteSource.Util.bytes(username),2).toString();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password,rememberMe); //将用户名，密码作为token
        ModelAndView flogin = new ModelAndView();
        flogin.setViewName("flogin");
        try {
            sub.login(token); //尝试登陆
        } catch (UnknownAccountException e) { //处理登陆错误的情况
            log.error("对用户[{}]进行登录验证,验证未通过,用户不存在", username);
            token.clear(); //刷新token
            flogin.addObject("result", "UnknownAccountException"); //设置键值对
            return flogin; //返回相应数据
        } catch (LockedAccountException lae) {
            log.error("对用户[{}]进行登录验证,验证未通过,账户已锁定", username);
            token.clear();
            flogin.addObject("result", "LockedAccountException");
            return flogin;
        } catch (ExcessiveAttemptsException e) {
            log.error("对用户[{}]进行登录验证,验证未通过,错误次数过多", username);
            token.clear();
            flogin.addObject("result", "ExcessiveAttemptsException");
            return flogin;
        } catch (AuthenticationException e) {
            log.error("对用户[{}]进行登录验证,验证未通过,堆栈轨迹如下", username, e);
            token.clear();
            flogin.addObject("result", "AuthenticationException");
            return flogin;
        }
        return new ModelAndView("redirect:/admin/articlepost"); //登陆成功则返回到/admin/articlepost
    }

}