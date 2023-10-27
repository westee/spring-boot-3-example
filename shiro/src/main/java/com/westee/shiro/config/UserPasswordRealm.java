package com.westee.shiro.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;

public class UserPasswordRealm extends AuthorizingRealm {
    @Override
    public String getName() {
        return LoginType.USER_PASSWORD.getType();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof UserToken) {
            return ((UserToken) token).getLoginType() == LoginType.USER_PASSWORD;
        } else {
            return false;
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("---------------- 用户密码登录 ----------------------");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 从数据库获取对应用户名密码的用户
        String name = token.getUsername();
        HashMap<String, String> user = new HashMap<>();
        user.put("loginFlag", "1");
        user.put("username", "1");
        user.put("password", "2");
        if (user != null) {
            // 用户为禁用状态
            if (!user.get("loginFlag").equals("1")) {
                throw new DisabledAccountException();
            }
            return new SimpleAuthenticationInfo(
                    user.get("username"),        //用户
                    user.get("password"),        //密码
                    getName()                    //realm name
            );
        }
        throw new UnknownAccountException();
    }
}
