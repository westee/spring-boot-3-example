package com.westee.springsecurity.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MyUser extends User {
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String encryptedPassword;
    private int sex;
    private int age;
    private String address;

    public MyUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String encryptedPassword) {
        super(username, password, authorities);
        this.encryptedPassword = encryptedPassword;
    }

    public MyUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String encryptedPassword) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.encryptedPassword = encryptedPassword;
    }

    public MyUser(String admin, String encode, Collection<? extends GrantedAuthority> list) {
        super(admin, encode, list);
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
