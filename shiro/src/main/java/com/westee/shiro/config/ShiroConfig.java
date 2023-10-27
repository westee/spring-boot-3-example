package com.westee.shiro.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.lang.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

@Configuration
public class ShiroConfig implements WebMvcConfigurer {
    private static final String SALT = "111";

    /**
     * 自定义的Realm管理，主要针对多realm
     */
    @Bean("myModularRealmAuthenticator")
    public MyModularRealmAuthenticator modularRealmAuthenticator() {
        MyModularRealmAuthenticator customizedModularRealmAuthenticator = new MyModularRealmAuthenticator();
        // 设置realm判断条件
        customizedModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        ArrayList<Realm> realms = new ArrayList<>();
        // 用户密码登录realm
        realms.add(userPasswordRealm());
        realms.add(wechatLoginRealm());
        realms.add(jwtRealm());
        customizedModularRealmAuthenticator.setRealms(realms);

        return customizedModularRealmAuthenticator;
    }

    @Bean
    public DefaultWebSecurityManager mySecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        ArrayList<Realm> realms = new ArrayList<>();
        // 用户密码登录realm
        realms.add(userPasswordRealm());
        realms.add(wechatLoginRealm());
        realms.add(jwtRealm());

        securityManager.setRealms(realms);

        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(1000L * 60 * 60 * 60 * 24 * 30); // 30天
        securityManager.setSessionManager(defaultWebSessionManager);
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setAuthenticator(modularRealmAuthenticator());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    /**
     * 密码登录realm
     *
     * @return UserPasswordRealm
     */
    @Bean
    public UserPasswordRealm userPasswordRealm() {
        UserPasswordRealm userPasswordRealm = new UserPasswordRealm();
        userPasswordRealm.setName(LoginType.USER_PASSWORD.getType());
        // 自定义的密码校验器
        userPasswordRealm.setCredentialsMatcher(credentialsMatcher());
        return userPasswordRealm;
    }

    @Bean
    public WechatLoginRealm wechatLoginRealm() {
        WechatLoginRealm wechatLoginRealm = new WechatLoginRealm();
        wechatLoginRealm.setName(LoginType.WECHAT_LOGIN.getType());
        return wechatLoginRealm;
    }

    @Bean
    public JWTRealm jwtRealm() {
        JWTRealm jwtRealm = new JWTRealm();
        jwtRealm.setName(LoginType.WECHAT_LOGIN.getType());
        return jwtRealm;
    }


    /**
     * 用于账号密码登录
     * 在SecurityUtils.getSubject.isAuthenticated()时运行
     * 用此方法进行身份验证时，Shiro会将您提供的凭据与在Subject中保存的凭据进行比较。
     * 数据库中的密码通常在SimpleAuthenticationInfo（包含从数据库获取的凭据）中保存，
     * 而CredentialsMatcher会将它们与用户提交的相应凭据进行比较。
     *
     * @return CredentialsMatcher
     */
    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return (authenticationToken, authenticationInfo) -> {
            String submittedPassword = new String((char[]) authenticationToken.getCredentials());
            String encryptedPassword = new Sha256Hash(submittedPassword, SALT).toString();
            String storedPassword = (String) authenticationInfo.getCredentials();
            return submittedPassword.equals(storedPassword);
        };
    }

    public CookieRememberMeManager rememberMeManager() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setMaxAge(86400);
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie);
        cookieRememberMeManager.setCipherKey(Base64.decode("xx"));  // RememberMe cookie encryption key default AES algorithm of key length (128, 256, 512)
        return cookieRememberMeManager;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        // need to accept POSTs from the login form
        chainDefinition.addPathDefinition("/login.html", "authc");
        chainDefinition.addPathDefinition("/logout", "logout");
        return chainDefinition;
    }
}
