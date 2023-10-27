package com.westee.shiro.config;

import com.westee.shiro.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class JWTRealm extends AuthorizingRealm {
    @Override
    public String getName() {
        return LoginType.USER_PHONE.getType();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof UserToken) {
            return ((UserToken) token).getLoginType() == LoginType.USER_PHONE;
        } else {
            return false;
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = JWTUtil.getUsername(principals.toString());
        //查询数据库，获取用户角色
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("role1");
        simpleAuthorizationInfo.addRole("role2");
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) auth;
        String name = token.getUsername();
        // 从数据库获取对应用户名密码的用户
//        User user = userService.getUserByName(name);
//        if (user != null) {
//            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//                    user, //用户
//                    user.getPassword(), //密码
//                    getName()  //realm name
//            );
//            return authenticationInfo;
//        }
//        throw new UnknownAccountException();
        return null;
    }
}
