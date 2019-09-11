package com.rkroom.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="site") //读取前缀
@Data
public class SiteInfo {
    private String title;
    private String url;
    private Integer port;
}
