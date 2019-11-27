package com.rkroom.blog.service.Impl

import com.rkroom.blog.entity.User
import com.rkroom.blog.repository.UserRepository
import com.rkroom.blog.service.UserService
import org.apache.shiro.crypto.hash.SimpleHash
import org.apache.shiro.util.ByteSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service //定义为服务
class UserServiceImpl : UserService {
    //实现userservice接口
    @Autowired
    var userRepository: //自动装配
            UserRepository? = null

    override fun selectByUsername(username: String?): User? {
        return userRepository!!.findByUsername(username) //根据用户名返回数据
    }

    override fun selectById(id: Int): User? {
        val optional = userRepository!!.findById(id)
        return optional.get()
    }

    override fun updatePasswordById(password: String?, id: Int): Int {
        var password = password
        val username = userRepository!!.findUsernameById(id)
        //将密码hash后存入数据库
        password = SimpleHash("md5", password, ByteSource.Util.bytes(username), 2).toString()
        userRepository!!.updatePasswordById(password, id)
        return 1
    }

    //新增用户
    override fun insertUser(user: User?) { // 将密码hash后再存入数据库
        val username = user!!.username
        var password = user.password
        password = SimpleHash("md5", password, ByteSource.Util.bytes(username), 2).toString()
        user.password = password
        userRepository!!.save(user)
    }
}