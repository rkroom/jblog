package com.rkroom.blog.config

import org.apache.shiro.cache.ehcache.EhCacheManager
import org.apache.shiro.spring.LifecycleBeanPostProcessor
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.servlet.Filter

@Configuration //定义配置类
class ShiroConfig {
    //设置配置为ehcache-shiro.xml
//设置缓存bean
    @get:Bean
    val ehCacheManager: EhCacheManager
        get() {
            val em = EhCacheManager()
            em.cacheManagerConfigFile = "classpath:ehcache-shiro.xml" //设置配置为ehcache-shiro.xml
            return em
        }

    //设置bean生命周期管理
    @get:Bean(name = ["lifecycleBeanPostProcessor"])
    val lifecycleBeanPostProcessor: LifecycleBeanPostProcessor
        get() = LifecycleBeanPostProcessor()

    //实现spring的自动代理
    @get:Bean
    val defaultAdvisorAutoProxyCreator: DefaultAdvisorAutoProxyCreator
        get() {
            val autoProxyCreator = DefaultAdvisorAutoProxyCreator()
            autoProxyCreator.isProxyTargetClass = true
            return autoProxyCreator
        }

    @Bean(name = ["myShiroRealm"]) //定义一个bean，命名为myShiroRealm
    fun myShiroRealm(cacheManager: EhCacheManager?): MyShiroRealm {
        val authRealm = MyShiroRealm() //新建一个MyShiroRealm对象
        authRealm.cacheManager = cacheManager //设置缓存为ehcache
        return authRealm
    }

    @Bean(name = ["securityManager"]) //shiro安全管理模块，并加载myShiroRealm模块
    fun getDefaultWebSecurityManager(myShiroRealm: MyShiroRealm?): DefaultWebSecurityManager {
        val defaultWebSecurityManager = DefaultWebSecurityManager()
        defaultWebSecurityManager.setRealm(myShiroRealm)
        return defaultWebSecurityManager
    }

    @Bean(name = ["shiroFilter"]) //shiro过滤器
    fun getShiroFilterFactoryBean(securityManager: DefaultWebSecurityManager?): ShiroFilterFactoryBean {
        val shiroFilterFactoryBean = ShiroFilterFactoryBean()
        //加载JWT过滤器
        val filterMap: MutableMap<String, Filter> = HashMap()
        filterMap["jwt"] = JWTFilter()
        shiroFilterFactoryBean.filters = filterMap
        shiroFilterFactoryBean.securityManager = securityManager
        loadShiroFilterChain(shiroFilterFactoryBean) //加载过滤链
        return shiroFilterFactoryBean
    }

    //user，需要登陆才可访问，anon任何人可以访问，authc，需要登陆时才使用，在设置访问权限的时候要注意顺序
    private fun loadShiroFilterChain(shiroFilterFactoryBean: ShiroFilterFactoryBean) {
        val filterChainDefinitionMap: MutableMap<String, String> = LinkedHashMap()
        // 使/api/路径下所有的请求都接受JWT的过滤
        filterChainDefinitionMap["/api/**"] = "jwt"
        filterChainDefinitionMap["/**"] = "anon"
        shiroFilterFactoryBean.filterChainDefinitionMap = filterChainDefinitionMap
    }
}