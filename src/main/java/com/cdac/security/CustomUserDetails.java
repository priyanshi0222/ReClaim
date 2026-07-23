package com.cdac.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cdac.entity.User;
import com.cdac.enums.AccountStatus;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns the wrapped User entity.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns user's role as Spring Security authority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    /**
     * Returns encrypted password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns username (email).
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Account expiry not implemented.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Account locking not implemented.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Credential expiry not implemented.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Only ACTIVE users can login.
     */
    @Override
    public boolean isEnabled() {
        return user.getAccountStatus() == AccountStatus.ACTIVE;
    }
}