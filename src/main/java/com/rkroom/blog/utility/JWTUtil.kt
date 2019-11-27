package com.rkroom.blog.utility

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import java.util.*

object JWTUtil {
    // 过期时间七天
    private const val EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000.toLong()

    /*
     * 生成签名,过期时间为EXPIRE_TIME，将密码hash之后用作签名
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    fun sign(username: String?, userId: Int, secret: String?): String? {
        return try {
            val date = Date(System.currentTimeMillis() + EXPIRE_TIME)
            val algorithm = Algorithm.HMAC256(secret)
            // 附带username和userId信息
            JWT.create()
                    .withClaim("username", username)
                    .withClaim("id", userId)
                    .withExpiresAt(date)
                    .sign(algorithm)
        } catch (e: Exception) {
            null
        }
    }

    /*
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    @JvmStatic
    fun verify(token: String?, username: String?, secret: String?): Boolean {
        return try {
            val algorithm = Algorithm.HMAC256(secret)
            // 解析并验证token
            val verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build()
            val jwt = verifier.verify(token)
            true
        } catch (exception: Exception) {
            false
        }
    }

    /*
     * 获得token中的信息，无需secret解密也能获得
     * @return token中包含的用户名
     */
    @JvmStatic
    fun getUsername(token: String?): String? {
        return try {
            val jwt = JWT.decode(token)
            jwt.getClaim("username").asString()
        } catch (e: JWTDecodeException) {
            null
        }
    }
}