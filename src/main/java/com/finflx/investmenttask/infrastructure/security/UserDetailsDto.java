package com.finflx.investmenttask.infrastructure.security;

import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.enumuration.UserRole;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsDto implements UserDetails {
    private final Long id;
    private final String password;
    private final String email;
    private final UserRole scopeType;
    private final List<GrantedAuthority> authorities;

    public UserDetailsDto(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.scopeType = user.getScopeType();

        List<GrantedAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(new SimpleGrantedAuthority(user.getScopeType().toString()));
        this.authorities = userAuthorities;
    }

    public UserDetailsDto(Claims claims) {
        this.id = Long.parseLong(claims.getId());
        this.email = claims.getSubject();
        this.scopeType = UserRole.valueOf(claims.get("scopeType").toString());
        this.password = "";

        List<GrantedAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(new SimpleGrantedAuthority(claims.get("scopeType").toString()));
        this.authorities = userAuthorities;
    }

    public User toUser() {
        User user = new User();
        user.setId(this.id);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setScopeType(this.scopeType);
        return user;
    }

    public Long getId() {
        return id;
    }

    public UserRole getScopeType() {
        return scopeType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
