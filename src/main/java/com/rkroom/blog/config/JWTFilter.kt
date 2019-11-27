package com.rkroom.blog.config

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod
import java.io.IOException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTFilter : BasicHttpAuthenticationFilter() {
    //代码的执行流程 preHandle -> isAccessAllowed -> isLoginAttempt -> executeLogin
    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    override fun isLoginAttempt(request: ServletRequest?, response: ServletResponse?): Boolean {
        val req = request as HttpServletRequest
        val authorization = req.getHeader("Authorization")
        return authorization != null
    }

    /**
     *
     */
    @Throws(Exception::class)
    override fun executeLogin(request: ServletRequest?, response: ServletResponse?): Boolean {
        val httpServletRequest = request as HttpServletRequest
        val authorization = httpServletRequest.getHeader("Authorization")
        val token = JWTToken(authorization)
        // 提交给realm进行登入，如果错误它会抛出异常并被捕获
        getSubject(request, response).login(token)
        // 如果没有抛出异常则代表登入成功，返回true
        return true
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    override fun isAccessAllowed(request: ServletRequest?, response: ServletResponse?, mappedValue: Any?): Boolean {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response)
            } catch (e: Exception) {
                response401(request, response)
            }
        }
        return true
    }

    /**
     * 对跨域提供支持
     */
    @Throws(Exception::class)
    override fun preHandle(request: ServletRequest?, response: ServletResponse?): Boolean {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"))
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE")
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"))
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.method == RequestMethod.OPTIONS.name) {
            httpServletResponse.status = HttpStatus.OK.value()
            return false
        }
        return super.preHandle(request, response)
    }

    /**
     * 将非法请求跳转到 /401
     */
    private fun response401(req: ServletRequest?, resp: ServletResponse?) {
        try {
            val httpServletResponse = resp as HttpServletResponse
            httpServletResponse.sendRedirect("/401")
        } catch (e: IOException) {
        }
    }
}