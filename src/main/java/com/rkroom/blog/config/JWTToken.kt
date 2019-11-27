package com.rkroom.blog.config

import org.apache.shiro.authc.AuthenticationToken

class JWTToken(// 密钥
//不论执行的getPrincipal()还是getCredentials()都会返回jwt的token
        private val token: String) : AuthenticationToken {

    override fun getPrincipal(): Any {
        return token
    }

    override fun getCredentials(): Any {
        return token
    }

}