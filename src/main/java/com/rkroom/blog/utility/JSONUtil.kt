package com.rkroom.blog.utility

import java.io.BufferedReader
import java.io.InputStreamReader
import javax.servlet.http.HttpServletRequest

object JSONUtil {
    fun getJSONString(request: HttpServletRequest?): String { // 数据写入Stringbuilder
        val sb = StringBuilder()
        try { // 获取输入流
            val streamReader = BufferedReader(InputStreamReader(request?.inputStream, "UTF-8"))
            // 从输入流获取字符串
            var line: String? = null
            while (streamReader.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //返回字符串
        return sb.toString()
    }
}