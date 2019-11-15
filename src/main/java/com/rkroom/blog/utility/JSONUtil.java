package com.rkroom.blog.utility;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JSONUtil {
    public static String getJSONString(HttpServletRequest request){
        // 数据写入Stringbuilder
        StringBuilder sb = new StringBuilder();
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            // 从输入流获取字符串
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回字符串
        return sb.toString();
    }
}