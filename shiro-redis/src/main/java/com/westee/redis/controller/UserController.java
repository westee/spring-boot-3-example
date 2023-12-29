package com.westee.redis.controller;

import com.westee.redis.model.UserInfo;
import com.westee.redis.service.UserInfoServiceImpl;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private static final String STATUS = "status";
    private static final String USERNAME = "username";

    private final UserInfoServiceImpl userInfoService;

    @Autowired
    public UserController(UserInfoServiceImpl userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public Map<String, String> login(@RequestBody UserInfo userInfo) {
        val response = new HashMap<String, String>();
        String loginStatus = userInfoService.login(userInfo.getUsername(), userInfo.getPassword());
        response.put(STATUS, loginStatus);
        response.put(USERNAME, userInfo.getUsername());
        return response;
    }

    @PostMapping(value = "/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserInfo> register(@RequestBody UserInfo userInfo) {
        val userInfoToReturn = userInfoService.register(userInfo.getUsername(), userInfo.getPassword());
        assert userInfoToReturn != null;
        return ResponseEntity.ok(userInfoToReturn);
    }

    @GetMapping("status")
    public Object getLoginStatus() {
        return userInfoService.getLoginInfo();
    }

    @GetMapping("logout")
    public Object doLogout() {
        return userInfoService.logout();
    }
}
