package com.rkroom.blog.utility

/*
返回的数据一般分为三个部分，状态码，返回信息，返回的数据
 */
class ResponseBean(// 状态码，用以表示运行状态，一般用200代表运行正常，5XX代表服务端错误
        var code: Int, // 返回信息
        var msg: String?, // 返回的数据
        var data: Any?)