package com.westee.redis.config;

import com.westee.redis.model.UserInfo;
import com.westee.redis.service.UserInfoService;
import com.westee.redis.service.UserInfoServiceImpl;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class UserRealm extends AuthorizingRealm {
    private final UserInfoService userInfoManager;

    @Autowired
    public UserRealm(UserInfoServiceImpl userInfoManager) {
        this.userInfoManager = userInfoManager;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        throw new UnsupportedOperationException("The method is unsupported.");
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        UserInfo userInfo = userInfoManager.getUserInfoByUserName(username);
        return new SimpleAuthenticationInfo(userInfo.getUsername(),
                userInfo.getPassword(),
                this.getName());
    }
}
