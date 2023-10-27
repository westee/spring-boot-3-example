package com.westee.shiro.config;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UserToken extends UsernamePasswordToken {
    private LoginType loginType;
    //微信code
    private String code;

    public UserToken(LoginType loginType, String username, String password) {
        super(username, password);
        this.loginType = loginType;
    }

    public UserToken(LoginType loginType, String username, String password, String code) {
        super(username, password);
        this.code = code;
        this.loginType = loginType;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
