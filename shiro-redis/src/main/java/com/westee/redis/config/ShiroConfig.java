package com.westee.redis.config;

import com.westee.redis.shiro.CustomHttpFilter;
import com.westee.redis.shiro.RedisSessionDao;
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {
    private static final String HASH_ALGORITHM_NAME = "SHA-256";
    private static final int HASH_ITERATIONS = 1000;
    private static final long GLOBAL_SESSION_TIMEOUT = 1000 * 60 * 60 * 24;

    @Bean
    public SecurityManager securityManager(Realm realm, @Qualifier("CustomSessionManager") SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realm);
        securityManager.setSessionManager(sessionManager);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            SecurityManager securityManager) {

        val shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        val filters = shiroFilterFactoryBean.getFilters();
        filters.put("custom", new CustomHttpFilter());
        filters.put("authc", new FormAuthenticationFilter());
        val shiroFilterDefinitionMap = new LinkedHashMap<String, String>();
        shiroFilterDefinitionMap.put("/register", "anon");
        shiroFilterDefinitionMap.put("/login", "custom");
        shiroFilterDefinitionMap.put("/status", "anon");
        shiroFilterDefinitionMap.put("/logout", "anon");

        shiroFilterDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterDefinitionMap);

        return shiroFilterFactoryBean;

    }

    @Bean("CustomSessionManager")
    public SessionManager sessionManager(RedisSessionDao sessionDao) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDao);
        sessionManager.setGlobalSessionTimeout(GLOBAL_SESSION_TIMEOUT);
        return sessionManager;
    }

    @Bean
    public HashedCredentialsMatcher matcher() {
        val matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(HASH_ALGORITHM_NAME);
        matcher.setHashIterations(HASH_ITERATIONS);
        matcher.setHashSalted(true);
        matcher.setStoredCredentialsHexEncoded(false);
        return matcher;
    }
}
