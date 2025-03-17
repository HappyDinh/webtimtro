package com.haui.web_demo.entities;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public class CustomerUserDetails implements UserDetails {


    private Collection<? extends GrantedAuthority> authorities;
    private long userID;
    private String username;
    private String phone;
    private String zalo;
    private String password;
    private String role;
    private String avatar;
    private String address;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private boolean credentialsNonExpired;

    public CustomerUserDetails() {
        super();
    }

    // Constructor to initialize CustomUserDetails from UserDetail
    public CustomerUserDetails(Userobject userObject, Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        this.userID = userObject.getUserID();
        this.username = userObject.getUsername();
        this.phone = userObject.getPhone();
        this.zalo = userObject.getZalo();
        this.password = userObject.getPassword();
        this.avatar = userObject.getAvatar();
        this.address = userObject.getAddress();
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }

    // Getters and Setters

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZalo() {
        return zalo;
    }

    public void setZalo(String zalo) {
        this.zalo = zalo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // UserDetails methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
//
//    private Userobject user;
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return user..stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//    }


    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Optional: Clear sensitive data like password
    public void eraseCredentials() {
        this.password = null;
    }
}