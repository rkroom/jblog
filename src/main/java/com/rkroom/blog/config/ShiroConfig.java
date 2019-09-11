package com.rkroom.blog.config;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration //定义配置类
public class ShiroConfig {
    @Bean//设置cookie
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe"); //cookie为rememberMe
        simpleCookie.setHttpOnly(true); //仅限http
        simpleCookie.setPath("/"); //设置作用范围
        simpleCookie.setMaxAge(604800); //设置生效时常，以秒为单位，这里是七天
        return simpleCookie;
    }

    @Bean //设置rememberme管理
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        /* key生成的方法，建议自行生成key保证服务的安全性。
        try{
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
           SecretKey deskey = keygen.generateKey();
           System.out.println(Base64.encodeToString(deskey.getEncoded()));}catch (Exception e) {
           System.out.println("exception:"+e.toString());
        }*/
        cookieRememberMeManager.setCipherKey(Base64.decode("S8+74LJR9whLdemqJbJeeA=="));
        return cookieRememberMeManager;
    }
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
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login"); //登陆页面的URL
        shiroFilterFactoryBean.setSuccessUrl("/admin/articlepost"); //登陆成功后跳转的URL
        shiroFilterFactoryBean.setUnauthorizedUrl("/denied"); //禁止登陆的URL
        loadShiroFilterChain(shiroFilterFactoryBean); //加载过滤链
        return shiroFilterFactoryBean;
    }

    //user，需要登陆才可访问，anon任何人可以访问，authc，需要登陆时才使用，在设置访问权限的时候要注意顺序
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/admin/**","user");
        filterChainDefinitionMap.put("/login", "authc");
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
}
