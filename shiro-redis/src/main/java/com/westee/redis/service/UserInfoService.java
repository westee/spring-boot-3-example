package com.westee.redis.service;

import com.westee.redis.model.UserInfo;

public interface UserInfoService {
    UserInfo getUserInfoByUserId(Long userId);

    UserInfo getUserInfoByUserName(String username);

    String login(String username, String password);

    UserInfo register(String username, String password);
}
