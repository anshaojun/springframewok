package com.personal.springframework.configuration;

import com.personal.springframework.constant.Constant;
import com.personal.springframework.shiro.CacheSessionDAO;
import com.personal.springframework.shiro.CustomRealm;
import com.personal.springframework.shiro.SessionDAO;
import com.personal.springframework.shiro.SessionManager;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: springframework
 * @description: shiro
 * @author: 安少军
 * @create: 2021-12-29 17:18
 **/
@Configuration
public class ShiroConfiguration {

    /**
     * @return org.apache.shiro.spring.web.ShiroFilterFactoryBean
     * @Author 安少军
     * @Description shiro的url过滤器
     * @Date 17:29 2021/12/29
     * @Param [securityManager]
     **/
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //没有登陆时转到登陆页面
        shiroFilterFactoryBean.setLoginUrl("/login/login.do");
        //没有权限的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth.do");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/login/**", "anon");
        filterChainDefinitionMap.put("/webservice/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/html/**", "anon");

        filterChainDefinitionMap.put("/", "authc");
        filterChainDefinitionMap.put("/admin/**", "authc");
        filterChainDefinitionMap.put("/user/**", "authc");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SessionDAO cacheSessionDAO() {
        CacheSessionDAO sessionDAO = new CacheSessionDAO();
        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        sessionDAO.setActiveSessionsCacheName("activeSession");
        sessionDAO.setCacheManager(getEhCacheManager());
        return sessionDAO;
    }

    /**
     * @return com.personal.springframework.shiro.SessionManager
     * @Author 安少军
     * @Description 自定义session管理器
     * @Date 12:09 2021/12/31
     * @Param []
     **/
    @Bean
    public SessionManager sessionManager() {
        SessionManager sessionManager = new SessionManager();
        //注入sessionDao
        sessionManager.setSessionDAO(cacheSessionDAO());
        //设置session失效时间
        sessionManager.setGlobalSessionTimeout(Constant.GLOBALSESSIONTIMEOUT);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }


    //防止spring的cache和Ehcache冲突
    @Bean
    @ConditionalOnMissingBean
    public net.sf.ehcache.CacheManager ehCacheCacheManager() {
        return CacheManager.create();
    }

    @Bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache.xml");
        return em;
    }

    /**
     * @return org.apache.shiro.mgt.SecurityManager
     * @Author 安少军
     * @Description 主要协调Shiro内部的各种安全组件
     * @Date 17:24 2021/12/29
     * @Param []
     **/
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(customRealm());
        defaultSecurityManager.setSessionManager(sessionManager());
        defaultSecurityManager.setCacheManager(getEhCacheManager());
        return defaultSecurityManager;
    }

    /**
     * @return com.personal.springframework.shiro.CustomRealm
     * @Author 安少军
     * @Description 用户数据和Shiro数据交互的桥梁。比如需要用户身份认证、权限认证。都是需要通过Realm来读取数据
     * @Date 17:24 2021/12/29
     * @Param []
     **/
    @Bean
    public CustomRealm customRealm() {
        CustomRealm shiroRealm = new CustomRealm();
        shiroRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        shiroRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        shiroRealm.setAuthenticationCacheName("authenticationCache");
        //启用受权缓存，即缓存AuthorizationInfo信息，默认false
        shiroRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        shiroRealm.setAuthorizationCacheName("authorizationCache");
        //配置自定义密码比较器
        //shiroRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
        return shiroRealm;
    }

    /**
     * @return org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
     * @Author 安少军
     * @Description 开启Shiro的注解(如 @ RequiresRoles, @ RequiresPermissions), 需借助SpringAOP扫描使用Shiro注解的类
     * @Date 17:29 2021/12/29
     * @Param []
     **/
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
