package com.westee.redis.service;

import com.westee.redis.model.UserInfo;
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    public static final int HASH_ITERATIONS = 1000;
    UserInfo userInfo = new UserInfo(1L, "1", "123", "123");

    @Override
    public UserInfo getUserInfoByUserId(Long userId) {
        return userInfo;
    }

    @Override
    public UserInfo getUserInfoByUserName(String username) {
        return userInfo;
    }

    @Override
    public String login(String username, String password) {
        // Get subject
        val subject = SecurityUtils.getSubject();

        // Construct token
        val usernamePasswordToken = new UsernamePasswordToken(username, password);
        //login

        try {
            subject.login(usernamePasswordToken);
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            session.setAttribute("username", username);
        } catch (IncorrectCredentialsException e) {
            return "密码错误";
        }
        return "登录成功";
    }

    @Override
    public UserInfo register(String username, String password) {
        String salt = "1";
        String encryptedPassword = new Sha256Hash(password, salt, HASH_ITERATIONS).toBase64();
        System.out.println(encryptedPassword);
        return userInfo;
    }

    public Object getLoginInfo() {
        HashMap<String, Object> result = new HashMap<>();

        val subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            Session session = subject.getSession();
            String username = (String) session.getAttribute("username");
            result.put("status", "已登录");
            result.put("username", username);
            return result;
        }
        result.put("status", "未登录");
        return result;
    }

    public Object logout() {
        val subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            // 清除用户对应的session
            Session session = subject.getSession();
            session.removeAttribute("username");
            subject.logout();
        }
        return "success";
    }
}
