package com.rkroom.blog.config;

import com.rkroom.blog.entity.Site;
import com.rkroom.blog.entity.User;
import com.rkroom.blog.service.SiteService;
import com.rkroom.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// 初始化数据库，每次重新运行springboot2程序时会执行一次
@Component
public class initDB implements ApplicationRunner {

    @Autowired
    private SiteService siteService;

    @Autowired
    private UserService userService;

    //从数据库获取网站信息，如果返回的信息为空，则认为网站需要初始化
    @Override
    public void run(ApplicationArguments var1) throws Exception {
        if(siteService.selectOpenInfo().size() == 0){
            //插入title和subtitle两条信息
            Site site1 = new Site();
            site1.setAttribute("title");
            site1.setValue("菜鸟学堂");
            site1.setAuthorization(false);
            siteService.insertSite(site1);
            Site site2 = new Site();
            site2.setAttribute("subtitle");
            site2.setValue("笨鸟先飞");
            site2.setAuthorization(false);
            siteService.insertSite(site2);
            //插入用户信息，其用户名为admin，密码为123456
            User user = new User();
            user.setUsername("admin");
            user.setPassword("123456");
            user.setRole("admin");
            userService.insertUser(user);
        }
    }
}
