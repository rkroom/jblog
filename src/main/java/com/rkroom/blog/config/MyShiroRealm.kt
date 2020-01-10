package com.rkroom.blog.config

import com.rkroom.blog.service.UserService
import com.rkroom.blog.utility.JWTUtil.getUsername
import com.rkroom.blog.utility.JWTUtil.verify
import org.apache.shiro.authc.*
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.util.*

@Component //把普通pojo实例化到spring容器中
@Configuration //定义为配置类
class MyShiroRealm : AuthorizingRealm() {
    //继承自AuthorizingRealm
    @Autowired //自动装配User服务
    private val userService: UserService? = null

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    override fun supports(token: AuthenticationToken): Boolean {
        return token is JWTToken || token is UsernamePasswordToken
    }

    @Throws(AuthenticationException::class)  //重写doGetAuthenticationInfo方法
    override fun doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo { //使用JWT的token进行登陆
        //获取token
        val authUser = token.credentials as String
        // 解密获得username，用于和数据库进行对比
        val username = getUsername(authUser) ?: throw AuthenticationException("token invalid")
        // 如果用户名为空，则抛出错误
        //获取用户信息
        val user = Optional.ofNullable(userService!!.selectByUsername(username)).orElseThrow { UnknownAccountException() }
        if (user.isLocked) { //如果用户被锁定
            throw LockedAccountException() //抛出锁定信息
        }
        // 如果验证为通过则提示用户名或密码错误
        if (!verify(authUser, username, user.password)) {
            throw AuthenticationException("Username or password error")
        }
        //返回验证信息
        return SimpleAuthenticationInfo(authUser, authUser, "my_realm")
    }

    //重写doGetAuthorizationInfo方法，doGetAuthorizationInfo方法主要是获取用户权限的，我们暂时不涉及这个方面的内容
    override fun doGetAuthorizationInfo(principal: PrincipalCollection): AuthorizationInfo {
        return SimpleAuthorizationInfo()
    }
}