package com.westee.springsecurity.auth;

import com.westee.springsecurity.entity.MyUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Component
@WebFilter
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");

        // 如果没有token，跳过该过滤器
        if (Objects.nonNull(token)) {
            // 模拟redis中的数据
            HashMap<String, MyUser> map = new HashMap<>();

            ArrayList<GrantedAuthority> list = new ArrayList<>();
            GrantedAuthority authority = () -> "p1";
            GrantedAuthority authority1 = () -> "p2";
            list.add(authority);
            list.add(authority1);

            ArrayList<GrantedAuthority> list1 = new ArrayList<>();
            list1.add(authority);

            map.put("test_token1", new MyUser("admin", new BCryptPasswordEncoder().encode("123456"), list));
            map.put("test_token2", new MyUser("root", new BCryptPasswordEncoder().encode("123456"), list1));

            // 这里模拟从redis获取token对应的用户信息
            MyUser myUser = map.get(token);
            if (myUser != null) {
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(myUser, null, myUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authRequest);
            } else {
                throw new PreAuthenticatedCredentialsNotFoundException("token不存在");
            }
        }
        filterChain.doFilter(request, response);
    }
}
