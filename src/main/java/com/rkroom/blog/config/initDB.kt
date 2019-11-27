package com.rkroom.blog.config

import com.rkroom.blog.entity.Site
import com.rkroom.blog.entity.User
import com.rkroom.blog.service.SiteService
import com.rkroom.blog.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

// 初始化数据库，每次重新运行springboot2程序时会执行一次
@Component
class initDB : ApplicationRunner {
    @Autowired
    private val siteService: SiteService? = null
    @Autowired
    private val userService: UserService? = null

    //从数据库获取网站信息，如果返回的信息为空，则认为网站需要初始化
    @Throws(Exception::class)
    override fun run(var1: ApplicationArguments) {
        if (siteService!!.selectByAttribute("title") == null) { //插入title和subtitle两条信息
            val site1 = Site()
            site1.attribute = "title"
            site1.value = "菜鸟学堂"
            site1.isAuthorization = false
            siteService.insertSite(site1)
            val site2 = Site()
            site2.attribute = "subtitle"
            site2.value = "笨鸟先飞"
            site2.isAuthorization = false
            siteService.insertSite(site2)
            //插入用户信息，其用户名为admin，密码为123456
            val user = User()
            user.username = "admin"
            user.password = "123456"
            user.role = "admin"
            userService!!.insertUser(user)
        }
    }
}