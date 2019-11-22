package com.rkroom.blog.config;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;

@Configuration //定义配置类
public class ShiroConfig {

    @Bean //设置缓存bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml"); //设置配置为ehcache-shiro.xml
        return em;
    }


    @Bean(name = "lifecycleBeanPostProcessor") //设置bean生命周期管理
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean //实现spring的自动代理
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }


    @Bean(name = "myShiroRealm") //定义一个bean，命名为myShiroRealm
    public MyShiroRealm myShiroRealm(EhCacheManager cacheManager) {
        MyShiroRealm authRealm = new MyShiroRealm();//新建一个MyShiroRealm对象
        authRealm.setCacheManager(cacheManager); //设置缓存为ehcache
        return authRealm;
    }

    @Bean(name = "securityManager") //shiro安全管理模块，并加载myShiroRealm模块
   public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroRealm myShiroRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(myShiroRealm);
        return defaultWebSecurityManager;
    }
    @Bean(name = "shiroFilter") //shiro过滤器
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //加载JWT过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JWTFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        loadShiroFilterChain(shiroFilterFactoryBean); //加载过滤链
        return shiroFilterFactoryBean;
    }

    //user，需要登陆才可访问，anon任何人可以访问，authc，需要登陆时才使用，在设置访问权限的时候要注意顺序
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 使/api/路径下所有的请求都接受JWT的过滤
        filterChainDefinitionMap.put("/api/**","jwt");
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
}
