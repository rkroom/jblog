package com.rkroom.blog.config

import com.rkroom.blog.utility.ResponseBean
import org.apache.shiro.ShiroException
import org.apache.shiro.authz.UnauthorizedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

//统一处理异常
@RestControllerAdvice
class ExceptionController {
    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException::class)
    fun handle401(e: ShiroException): ResponseBean {
        return ResponseBean(401, e.message, null)
    }

    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException::class)
    fun handle401(): ResponseBean {
        return ResponseBean(401, "Unauthorized", null)
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun globalException(request: HttpServletRequest, ex: Throwable): ResponseBean {
        return ResponseBean(getStatus(request).value(), ex.message, null)
    }

    //获取状态码
    private fun getStatus(request: HttpServletRequest): HttpStatus {
        val statusCode = request.getAttribute("javax.servlet.error.status_code") as Int
                ?: return HttpStatus.INTERNAL_SERVER_ERROR
        return HttpStatus.valueOf(statusCode)
    }
}