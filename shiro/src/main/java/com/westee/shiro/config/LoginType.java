package com.westee.shiro.config;

public enum LoginType {
    /**
     * 用户密码登录
     */
    USER_PASSWORD("user_password_realm"),
    /**
     * 手机验证码登录
     */
    USER_PHONE("user_phone_realm"),
    /**
     * 第三方登录(微信登录)
     */
    WECHAT_LOGIN("wechat_login_realm");

    private String type;

    LoginType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
