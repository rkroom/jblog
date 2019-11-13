package com.rkroom.blog.utility;

import lombok.Data;

/*
返回的数据一般分为三个部分，状态码，返回信息，返回的数据
 */
@Data //自动生成GET和SET方法
public class ResponseBean {

    // 状态码，用以表示运行状态，一般用200代表运行正常，5XX代表服务端错误
    private int code;

    // 返回信息
    private String msg;

    // 返回的数据
    private Object data;

    public ResponseBean(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}