package com.westee.shiro.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.westee.shiro.config.LoginType;
import com.westee.shiro.config.UserService;
import com.westee.shiro.config.UserToken;
import com.westee.shiro.exception.HttpException;
import com.westee.shiro.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("/login")
@RestController
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/password")
    public HashMap<Object, Object> login(String username, String password) {
        UserToken userToken = new UserToken(LoginType.USER_PASSWORD, username, password);
        return shiroLogin(userToken, LoginType.USER_PASSWORD);
    }

    @GetMapping("/token")
    public String login(String token) {
        return JWTUtil.getUsername(token);
    }

    @GetMapping("/wechat")
    public HashMap<Object, Object> wechatLogin(@RequestParam String code) throws JsonProcessingException {
        UserService.WeChatSession weChatSession = userService.getWeChatSession(code);

        String openId = weChatSession.getOpenid();
        UserToken token;
        if (openId == null) {
            throw HttpException.badRequest("获取openId失败");
        } else {
            token = new UserToken(LoginType.WECHAT_LOGIN, openId, code, code);
            try {
                return shiroLogin(token, LoginType.WECHAT_LOGIN);
            } catch (AccountException accountException) {
                throw HttpException.badRequest(accountException.getMessage());
            }
        }
    }

    public HashMap<Object, Object> shiroLogin(UserToken token, LoginType type) {
        HashMap<Object, Object> map = new HashMap<>();

        try {
            //登录不在该处处理，交由shiro处理
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);

            if (subject.isAuthenticated()) {
                String jwtToken = JWTUtil.sign(token.getUsername(), type); //使用JWTRealm生成token
                Session session = subject.getSession();
                session.setAttribute("user", "userByName");
                map.put("message", "登录成功");
                map.put("token", jwtToken);
                return map;
            } else {
                map.put("message", "用户名密码不匹配");
                return map;
            }
        } catch (IncorrectCredentialsException | UnknownAccountException e) {
            e.printStackTrace();
            map.put("message", "用户名密码不匹配");
            return map;
        } catch (LockedAccountException e) {
            map.put("message", "用户已被锁定");
            return map;
        } catch (AuthenticationException e) {
            map.put("message", "用户名或密码不正确");
        } catch (Exception e) {
            map.put("message", e.getMessage());
            return map;
        }
        return map;
    }
}
