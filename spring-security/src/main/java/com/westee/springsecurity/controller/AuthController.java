package com.westee.springsecurity.controller;

import com.westee.springsecurity.auth.MobileCodeAuthenticationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        try {
            authenticationManager.authenticate(authenticationRequest);
        } catch (BadCredentialsException e) {
            System.out.println("账号密码不正确");
            return "账号密码不正确";
        }
        return "密码登录成功";
    }


    /**
     * 用户名密码登录
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/usernamePwd1")
    public String usernamePwd(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            e.printStackTrace();
            return "登录失败1";
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        return "登录成功：token" + token;
    }

    /**
     * 手机验证码登录
     *
     * @param phone
     * @param mobileCode
     * @return
     */
    @GetMapping("/mobileCode")
    public String mobileCode(String phone, String mobileCode) {
        MobileCodeAuthenticationToken mobilecodeAuthenticationToken = new MobileCodeAuthenticationToken(phone, mobileCode);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(mobilecodeAuthenticationToken);
        } catch (Exception e) {
            e.printStackTrace();
            return "验证码错误";
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        return "登录成功：token" + token;
    }

}
