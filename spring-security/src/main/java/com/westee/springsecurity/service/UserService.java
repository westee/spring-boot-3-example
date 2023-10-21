package com.westee.springsecurity.service;

import com.westee.springsecurity.entity.MyUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = getUserByUsername(username);
        if (myUser == null) {
            throw new UsernameNotFoundException(username + "不存在");
        }

        return new org.springframework.security.core.userdetails.User(
                username, myUser.getEncryptedPassword(), Collections.emptyList());
    }

    public MyUser getUserByUsername(String username) {
        return null;
    }
}
