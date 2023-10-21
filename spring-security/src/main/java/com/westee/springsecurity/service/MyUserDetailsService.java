package com.westee.springsecurity.service;

import com.westee.springsecurity.entity.MyUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        MyUser myUser;
        ArrayList<GrantedAuthority> list = new ArrayList<>();
        GrantedAuthority authority = () -> "p1";
        GrantedAuthority authority1 = () -> "p2";
        list.add(authority);
        list.add(authority1);
        // 这里模拟从数据库中获取用户信息
        if (username.equals("admin")) {
            myUser = new MyUser("admin", new BCryptPasswordEncoder().encode("123456"), list);
            myUser.setAge(25);
            myUser.setSex(1);
            myUser.setAddress("xxxx小区");
            return myUser;
        } else {
            throw new UsernameNotFoundException("用户不存在");
        }
    }
}
