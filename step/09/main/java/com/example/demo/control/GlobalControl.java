package com.example.demo.control;

import com.example.demo.config.SiteInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice //全局处理注解

public class GlobalControl {

    @Autowired
    private SiteInfo siteInfo;

    @ModelAttribute(name = "siteinfo")
    public SiteInfo siteInfo(){
        return siteInfo;
    }

    @ModelAttribute(name = "loginstatus") //设置属性名为loginstatus
    public boolean loginstatus() {
        /*获取登陆用户的用户名，如果用户名存在，则已经有用户登陆，如果用户名不存在则无用户登陆*/
        String loginName = (String) SecurityUtils.getSubject().getPrincipal();
        boolean loginstatus;
        if (loginName != null) {
            return true;
        }else {
            return false;
        }
    }
}
